package org.example.hw8_message_broadcast_systems

import org.example.hw3_abstractions.Property
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.commands.GameCommand

object GameScheduler {

    private val gamesMap: MutableMap<Long, GameCommand> = mutableMapOf()

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
}
