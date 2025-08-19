package org.example.utils

import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*

class KafkaProducer {

    private val kafkaProps = Properties().apply {
        put("bootstrap.servers", "localhost:29092")
        put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    val instance: KafkaProducer<String, String> by lazy {
        KafkaProducer<String, String>(kafkaProps)
    }
}
