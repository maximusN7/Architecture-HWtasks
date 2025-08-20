package org.example

import org.example.core.utils.InitApplication
import org.example.di.AppInjector

fun main() {
    InitApplication().init()
    AppInjector.component.getClientLooper().startProcess()
}
