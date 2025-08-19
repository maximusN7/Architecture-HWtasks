package org.example.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@Serializable
data class Message(
    val gameId: Long?,
    val gameObjectId: Long?,
    val operationId: Long,
    val args: Map<String, String?>,
) {

    companion object {

        fun String.parseToMessage(): Message {
            println("AAAAA parseToMessage parseToMessage $this")
            val jsonObj = Json.parseToJsonElement(this).jsonObject
            val gameId = jsonObj[idsListKeys[0]]?.jsonPrimitive?.long
            val gameObjectId = jsonObj[idsListKeys[1]]?.jsonPrimitive?.long
            val operationId = jsonObj[idsListKeys[2]]?.jsonPrimitive?.long ?: error("Missing 'operationId' field")
            val args = jsonObj.filterKeys { !idsListKeys.contains(it) }.mapValues { it.value.jsonPrimitive.content }

            return Message(
                gameId = gameId,
                gameObjectId = gameObjectId,
                operationId = operationId,
                args = args,
            )
        }

        private val idsListKeys = listOf("gameId", "gameObjectId", "operationId")
    }
}
