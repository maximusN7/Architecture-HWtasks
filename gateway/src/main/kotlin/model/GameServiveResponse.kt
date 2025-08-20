package org.example.model

data class GameServiveResponse(
    val isSuccess: Boolean,
    val body: String? = null,
    val code: Int? = null,
    val error: Exception? = null,
)
