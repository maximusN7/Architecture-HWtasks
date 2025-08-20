package org.example.processors.registered

import com.google.gson.Gson
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.NetworkClient
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.model.GameData
import javax.inject.Inject

class CreateGameCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        val gameName = parts.getOrNull(1)

        val participantsNames = if (parts.size > 2) {
            parts.subList(2, parts.size)
        } else {
            null
        }

        if (gameName != null && participantsNames != null) {
            val createGameRequest = CreateGameRequest(
                gameName = gameName,
                participantsNames = participantsNames,
            )

            val response = networkClient.sendJsonToMiddleware(Gson().toJson(createGameRequest), GAME_CREATION_PATH)
            if (response.isSuccess) {
                val gameData = Gson().fromJson(response.body, GameData::class.java)

                if (gameData.gameId != null) {
                    println("Game ${gameData.gameName} was created. To join the game use command: join <your password> ${gameData.gameId}")
                } else {
                    if (gameData.participantsNames.isEmpty()) {
                        println("Game ${gameData.gameName} was not created. There is no such participants in system")
                    } else {
                        println("Game ${gameData.gameName} was not created. Try again later")
                    }
                }
            } else {
                return ExceptionHandler.handle(this, response.error).process(currentState, parts)
            }
        } else {
            println("Use command: create <gameName> <participants names separated with space>")
        }

        return currentState
    }
}

data class CreateGameRequest(
    val gameName: String,
    val participantsNames: List<String>,
)

private const val GAME_CREATION_PATH = "/games/register"
