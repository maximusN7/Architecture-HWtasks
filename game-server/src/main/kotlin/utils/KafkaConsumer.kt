package org.example.utils

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KafkaConsumer @Inject constructor() {

    private val commandsConsumerProps by lazy {
        Properties().apply {
            put("bootstrap.servers", "localhost:29092")
            put("group.id", "game-server")
            put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            put("auto.offset.reset", "earliest")
        }
    }

    val instance: KafkaConsumer<String, String> by lazy {
        KafkaConsumer<String, String>(commandsConsumerProps)
    }
}
