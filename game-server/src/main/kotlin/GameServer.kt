package org.example

import org.example.di.AppInjector
import org.example.utils.InitApplication

fun main() {
    InitApplication().init()
    AppInjector.component.getServerLooper().startProcess()
}
