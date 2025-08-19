package org.example.core.utils

import org.example.di.AppInjector
import org.example.exceptions.AuthErrorException
import org.example.processors.unregistered.LoginCommandProcessor

class InitApplication {

    fun init() {
        AppInjector.component

        ExceptionHandler.register(
            LoginCommandProcessor::class.java,
            Exception::class.java,
        ) {
            AppInjector.component.getLogExceptionProcessor()
        }

        ExceptionHandler.register(
            LoginCommandProcessor::class.java,
            AuthErrorException::class.java,
        ) {
            AppInjector.component.getAuthErrorExceptionProcessor()
        }

        println("SpaceShips is launched and ready! Type in commands with keyboard")
    }
}
