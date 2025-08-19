package org.example.model

sealed class ApiRequest(val gameId: Long, val gameObjectId: Long, val operationId: Long, val token: String)

class MoveRequest(
    gameId: Long,
    gameObjectId: Long,
    operationId: Long,
    token: String,
    val velocity: String,
) : ApiRequest(gameId, gameObjectId, operationId, token)

class RotateRequest(
    gameId: Long,
    gameObjectId: Long,
    operationId: Long,
    token: String,
    val angularVelocity: String,
) : ApiRequest(gameId, gameObjectId, operationId, token)

class ShootRequest(
    gameId: Long,
    gameObjectId: Long,
    operationId: Long,
    token: String,
) : ApiRequest(gameId, gameObjectId, operationId, token)
