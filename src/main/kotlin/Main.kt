package org.example

import org.example.hw8_message_broadcast_systems.ConsumerEndpoint
import org.example.hw8_message_broadcast_systems.setup.InitEndpointIoC

fun main() {
    InitEndpointIoC.invoke()
    ConsumerEndpoint.startObserving()
}
