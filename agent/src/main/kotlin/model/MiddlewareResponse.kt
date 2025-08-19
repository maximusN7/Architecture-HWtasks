package org.example.model

data class MiddlewareResponse(
    val isSuccess: Boolean,
    val body: String? = null,
    val error: Exception? = null,
)