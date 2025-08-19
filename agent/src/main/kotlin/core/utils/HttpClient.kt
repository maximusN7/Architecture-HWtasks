package org.example.core.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpClient @Inject constructor() {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    fun makeRequest(requestJson: String, path: String): Response {
        val requestBody = requestJson.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$MIDDLEWARE_HOST$path")
            .post(requestBody)
            .build()

        return httpClient.newCall(request).execute()
    }
}

private const val MIDDLEWARE_HOST = "http://localhost:8083"
