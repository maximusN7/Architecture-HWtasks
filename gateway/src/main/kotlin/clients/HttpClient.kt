package org.example.clients

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.example.model.AuthServiceResponse
import org.example.model.GameServiveResponse
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HttpClient {

    private val httpClient by lazy {
        okhttp3.OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    fun sendJsonToAuthService(requestJson: String, path: String): AuthServiceResponse {
        return try {
            val requestBody = requestJson.toRequestBody("application/json".toMediaType())

            val request = okhttp3.Request.Builder()
                .url("$AUTH_SERVICE_HOST$path")
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()

            AuthServiceResponse(
                isSuccess = response.isSuccessful,
                body = response.body?.string(),
                code = response.code,
            )
        } catch (e: Exception) {
            AuthServiceResponse(
                isSuccess = false,
                error = e
            )
        }
    }

    fun sendJsonToGameService(requestJson: String, path: String): GameServiveResponse {
        return try {
            val requestBody = requestJson.toRequestBody("application/json".toMediaType())

            val request = okhttp3.Request.Builder()
                .url("$GAME_SERVICE_PATH$path")
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()

            GameServiveResponse(
                isSuccess = response.isSuccessful,
                body = response.body?.string(),
                code = response.code,
            )
        } catch (e: Exception) {
            GameServiveResponse(
                isSuccess = false,
                error = e
            )
        }
    }
}

private const val AUTH_SERVICE_HOST = "http://localhost:8081/auth"
private const val GAME_SERVICE_PATH = "http://localhost:8081/games"
