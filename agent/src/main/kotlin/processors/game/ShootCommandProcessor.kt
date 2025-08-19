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

class ShootCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
    private val gameCommandInterpreter: GameCommandInterpreter,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {

        val shootRequest = ShootRequest(
            gameId = (currentState as InGameState).currentGameId,
            gameObjectId = 1,
            operationId = gameCommandInterpreter.interpret(GamePlayCommand.SHOOT),
            token = currentState.userToken.token,
        )

        val response = networkClient.sendJsonToMiddleware(Gson().toJson(shootRequest), SHOOT_COMMAND_PATH)
        if (response.isSuccess) {

            println("Spaceship has shot")
            return currentState
        } else {
            return ExceptionHandler.handle(this, response.error).process(currentState, parts)
        }
    }
}

data class ShootRequest(
    val gameId: Long,
    val gameObjectId: Long,
    val operationId: Long,
    val token: String
)

private const val SHOOT_COMMAND_PATH = "/api/shoot"
