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

class RotateCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
    private val gameCommandInterpreter: GameCommandInterpreter,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        val angularVelocity = parts.getOrNull(1)?.toInt()

        if (angularVelocity != null) {
            val rotateRequest = RotateRequest(
                gameId = (currentState as InGameState).currentGameId,
                gameObjectId = 1,
                operationId = gameCommandInterpreter.interpret(GamePlayCommand.ROTATE),
                angularVelocity = angularVelocity,
                token = currentState.userToken.token,
            )

            val response = networkClient.sendJsonToMiddleware(Gson().toJson(rotateRequest), ROTATE_COMMAND_PATH)
            if (response.isSuccess) {

                println("Spaceship has rotate")
                return currentState
            } else {
                return ExceptionHandler.handle(this, response.error).process(currentState, parts)
            }
        } else {
            println("Use command: rotate <angular velocity>")
        }

        return currentState
    }
}

data class RotateRequest(
    val gameId: Long,
    val gameObjectId: Long,
    val operationId: Long,
    val angularVelocity: Int,
    val token: String
)

private const val ROTATE_COMMAND_PATH = "/api/rotate"
