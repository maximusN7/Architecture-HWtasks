package org.example.clients

import org.apache.kafka.clients.producer.ProducerRecord
import org.example.clients.utils.KafkaProducer
import org.example.model.KafkaMessage
import org.springframework.stereotype.Component

@Component
class KafkaClient(
    private val kafkaProducer: KafkaProducer
) {

    fun sendMessageToGameServer(message: KafkaMessage) {
        if (message.value.isNotBlank()) {
            kafkaProducer.instance.send(ProducerRecord(GAME_SERVER_COMMAND_TOPIC, message.key, message.value))
        }
    }
}

private const val GAME_SERVER_COMMAND_TOPIC = "command"
