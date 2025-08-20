package org.example.service

import org.example.model.KafkaMessage
import org.example.model.ApiRequest
import org.example.model.MoveRequest
import org.example.model.RotateRequest
import org.example.model.ShootRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ApiService {

    fun mapToMessage(request: MoveRequest): KafkaMessage {
        val messageId = UUID.randomUUID().toString()

        val message = StringBuilder().addCommonPart(request)
        message.append(""","velocity":${request.velocity}""")

        return KafkaMessage(
            key = messageId,
            value = message.closeMessage(),
        )
    }

    fun mapToMessage(request: RotateRequest): KafkaMessage {
        val messageId = UUID.randomUUID().toString()

        val message = StringBuilder().addCommonPart(request)
        message.append(""","angularVelocity":${request.angularVelocity}""")

        return KafkaMessage(
            key = messageId,
            value = message.closeMessage(),
        )
    }

    fun mapToMessage(request: ShootRequest): KafkaMessage {
        val messageId = UUID.randomUUID().toString()

        val message = StringBuilder().addCommonPart(request)

        return KafkaMessage(
            key = messageId,
            value = message.closeMessage(),
        )
    }

    private fun StringBuilder.addCommonPart(request: ApiRequest): StringBuilder {
        this.append("""{"gameId":${request.gameId},"gameObjectId":${request.gameObjectId},"operationId":${request.operationId},"token":"${request.token}"""")
        return this
    }

    private fun StringBuilder.closeMessage(): String {
        this.append("}")
        return this.toString()
    }
}
