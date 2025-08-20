package org.example.processors.registered

import com.google.gson.Gson
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.NetworkClient
import org.example.core.utils.InGameState
import org.example.core.utils.InMenuStateRegistered
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.model.Token
import javax.inject.Inject

class JoinGameCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        val currentUser = (currentState as? InMenuStateRegistered)?.currentUser
            ?: return ExceptionHandler.handle(this, IllegalArgumentException("null user")).process(currentState, parts)
        val password = parts.getOrNull(1)
        val gameId = parts.getOrNull(2)?.toLongOrNull()

        if (password != null && gameId != null) {
            val joinGameRequest = JoinGameRequest(
                username = currentUser.username,
                password = password,
                gameId = gameId,
            )

            val response = networkClient.sendJsonToMiddleware(Gson().toJson(joinGameRequest), GAME_JOIN_PATH)
            if (response.isSuccess) {
                val tokenData = Gson().fromJson(response.body, Token::class.java)

                println("Congrats! You have join the game $gameId")
                return InGameState(
                    currentGameId = gameId.toLong(),
                    currentUser = currentUser,
                    userToken = tokenData,
                )
            } else {
                return ExceptionHandler.handle(this, response.error).process(currentState, parts)
            }
        } else {
            println("Use command: join <your password> <gameId>")
        }

        return currentState
    }
}

data class JoinGameRequest(
    val username: String,
    val password: String,
    val gameId: Long,
)

private const val GAME_JOIN_PATH = "/games/join"
