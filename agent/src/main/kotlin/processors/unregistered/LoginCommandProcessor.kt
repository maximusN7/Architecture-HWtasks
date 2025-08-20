package org.example.processors.unregistered

import com.google.gson.Gson
import org.example.core.utils.ExceptionHandler
import org.example.core.utils.NetworkClient
import org.example.core.utils.InMenuStateRegistered
import org.example.contract.IClientState
import org.example.contract.ICommandProcessor
import org.example.core.utils.InMenuStateUnregistered
import org.example.model.UserData
import javax.inject.Inject

class LoginCommandProcessor @Inject constructor(
    private val networkClient: NetworkClient,
) : ICommandProcessor {

    override fun process(currentState: IClientState?, parts: List<String>): IClientState? {
        (currentState as InMenuStateUnregistered)
        val username = parts.getOrNull(1)
        val password = parts.getOrNull(2)

        if (username != null && password != null) {
            val loginRequest = LoginRequest(username, password)

            val response = networkClient.sendJsonToMiddleware(Gson().toJson(loginRequest), SIGN_IN_PATH)
            if (response.isSuccess) {
                val userData = Gson().fromJson(response.body, UserData::class.java)

                if (userData.isNewUser == null) {
                    println("It's wrong password for $username!")
                    return currentState
                }

                if (userData.isNewUser) {
                    println("Welcome, $username! To create a game use command: create <gameName> <participants names separated with space>")
                } else {
                    println("Welcome back, $username!")
                }

                return InMenuStateRegistered(userData)
            } else {
                return ExceptionHandler.handle(this, response.error).process(currentState, parts)
            }
        } else {
            println("Use command: login <user> <password>")
        }
        return currentState
    }
}

data class LoginRequest(
    val username: String,
    val password: String,
)

private const val SIGN_IN_PATH = "/auth/signin"
