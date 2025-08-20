package org.example.core.utils

import org.example.interpreters.ErrorCodeInterpreter
import org.example.model.MiddlewareResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkClient @Inject constructor(
    private val httpClient: HttpClient,
    private val errorCodeInterpreter: ErrorCodeInterpreter,
) {

    fun sendJsonToMiddleware(requestJson: String, path: String): MiddlewareResponse {
        return try {
            val response = httpClient.makeRequest(requestJson, path)

            val error = if (!response.isSuccessful) {
                errorCodeInterpreter.interpret(response.code)
            } else {
                null
            }

            MiddlewareResponse(
                isSuccess = response.isSuccessful,
                body = response.body?.string(),
                error = error
            )
        } catch (e: Exception) {
            MiddlewareResponse(
                isSuccess = false,
                error = e
            )
        }
    }
}
