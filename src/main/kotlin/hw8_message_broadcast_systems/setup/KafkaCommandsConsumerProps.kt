package org.example.hw8_message_broadcast_systems.setup

import java.util.*

val commandsConsumerProps by lazy {
    Properties().apply {
        put("bootstrap.servers", "localhost:29092")
        put("group.id", "kotlin-consumer-group")
        put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        put("auto.offset.reset", "earliest")
    }
}

val commandsConsumerTopics by lazy {
    listOf("command")
}
