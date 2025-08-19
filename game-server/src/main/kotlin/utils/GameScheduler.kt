package org.example.utils

import org.example.command.GameCommand
import ioc.ioc.IScope
import ioc.ioc.IoC
import org.example.model.Angle
import org.example.model.Property
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.com.google.gson.Gson
import java.awt.Point
import java.util.*

object GameScheduler {

    private val gamesMap: MutableMap<Long, GameCommand> = mutableMapOf()

    private val gameStateNotifier = GameStateNotifier(KafkaProducer())

    fun hasWork(): Boolean {
        for ((id, game) in gamesMap) {
            if (game.hasWork()) return true
        }
        return false
    }

    fun execute() {
        for ((id, game) in gamesMap) {
            if (game.hasWork()) {
                game.invoke()

                gameStateNotifier.notifyStateChanged(mapToKafkaMessage(id, game))
            }
        }
    }

    fun getGameById(id: Long, baseScope: IScope): GameCommand {
        val game = gamesMap[id]
        return if (game == null) {
            val scope = IoC.resolve("Scopes.New", baseScope) as IScope
            val newGame = GameCommand(scope)
            gamesMap[id] = newGame
            newGame
        } else {
            game
        }
    }

    @TestOnly
    fun getGameById(gameId: Long): GameCommand? {
        return gamesMap[gameId]
    }

    @TestOnly
    fun clearGames() {
        gamesMap.clear()
    }

    private fun mapToKafkaMessage(id: Long, game: GameCommand): KafkaMessage {
        val messageId = UUID.randomUUID().toString()

        val gameObjects = game.userObjects.mapNotNull {
            val obj = it.value[1]
            val position = obj?.getProperty(Property.LOCATION) as? Point
            val angle = obj?.getProperty(Property.ANGLE) as? Angle

            if (obj == null || position == null || angle == null) {
                null
            } else {
                GameObject(
                    username = it.key,
                    x = position.x,
                    y = position.y,
                    angle = angle.getAngleRDegrees()
                )
            }
        }

        return KafkaMessage(
            key = messageId,
            value = Gson().toJson(GameObjects(id, gameObjects)).toString(),
        )
    }
}

data class GameObjects(
    val gameId: Long,
    val gameObjects: List<GameObject?>
)

data class GameObject(
    val username: String,
    val x: Int,
    val y: Int,
    val angle: Int,
)
