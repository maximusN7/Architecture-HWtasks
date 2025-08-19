package org.example.model

data class AuthServiceResponse(
    val isSuccess: Boolean,
    val body: String? = null,
    val code: Int? = null,
    val error: Exception? = null,
)
