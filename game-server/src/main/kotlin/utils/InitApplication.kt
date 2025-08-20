package org.example.utils

import org.example.di.AppInjector
import org.example.utils.setup.InitEndpointIoC

class InitApplication {

    fun init() {
        AppInjector.component
        InitEndpointIoC.invoke()

        println("Game server is settled up")
    }
}
