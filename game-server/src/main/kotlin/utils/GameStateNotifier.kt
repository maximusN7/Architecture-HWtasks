package org.example.utils

import org.apache.kafka.clients.producer.ProducerRecord

class GameStateNotifier(
    private val kafkaProducer: KafkaProducer
) {

    fun notifyStateChanged(message: KafkaMessage) {
        if (message.value.isNotBlank()) {
            kafkaProducer.instance.send(ProducerRecord(GAME_SERVER_STATE_TOPIC, message.key, message.value))
        }
    }
}

data class KafkaMessage(
    val key: String,
    val value: String,
)

private const val GAME_SERVER_STATE_TOPIC = "game_state"
