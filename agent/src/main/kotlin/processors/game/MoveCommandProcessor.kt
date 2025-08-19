package org.example.processors.game

import com.google.gson.Gson
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.NetworkClient
import org.example.core.utils.InGameState
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.interpreters.GameCommandInterpreter
import org.example.interpreters.GamePlayCommand
import javax.inject.Inject

class MoveCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
    private val gameCommandInterpreter: GameCommandInterpreter,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        val velocity = parts.getOrNull(1)?.toInt()

        if (velocity != null) {
            val moveRequest = MoveRequest(
                gameId = (currentState as InGameState).currentGameId,
                gameObjectId = 1L,
                operationId = gameCommandInterpreter.interpret(GamePlayCommand.MOVE),
                velocity = velocity,
                token = currentState.userToken.token,
            )

            val response = networkClient.sendJsonToMiddleware(Gson().toJson(moveRequest), MOVE_COMMAND_PATH)
            if (response.isSuccess) {

                println("Spaceship has moved")
                return currentState
            } else {
                return ExceptionHandler.handle(this, response.error).process(currentState, parts)
            }
        } else {
            println("Use command: move <velocity>")
        }

        return currentState
    }
}

data class MoveRequest(
    val gameId: Long,
    val gameObjectId: Long,
    val operationId: Long,
    val velocity: Int,
    val token: String
)

private const val MOVE_COMMAND_PATH = "/api/move"
