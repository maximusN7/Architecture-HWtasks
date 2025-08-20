package org.example.di

import dagger.Component
import org.example.core.ClientLooper
import org.example.processors.*
import org.example.exceptions.LogExceptionCommandProcessor
import org.example.processors.game.*
import org.example.processors.registered.CreateGameCommandProcessor
import org.example.processors.registered.HelpForRegisteredCommandProcessor
import org.example.processors.registered.JoinGameCommandProcessor
import org.example.processors.registered.LogoutProgramCommandProcessor
import org.example.processors.unregistered.GameFromUnregisteredCommandProcessor
import org.example.processors.unregistered.HelpForUnregisteredCommandProcessor
import org.example.processors.unregistered.LoginCommandProcessor
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    fun getClientLooper(): ClientLooper

    // region CommandProcessors

    fun getLoginProcessor(): LoginCommandProcessor

    fun getLogExceptionProcessor(): LogExceptionCommandProcessor

    fun getAuthErrorExceptionProcessor(): AuthErrorExceptionProcessor

    fun getUnknownProcessor(): UnknownCommandProcessor

    fun getExitProgramProcessor(): ExitProgramCommandProcessor

    fun getLogoutProgramProcessor(): LogoutProgramCommandProcessor

    fun getHelpForUnregisteredProcessor(): HelpForUnregisteredCommandProcessor

    fun getHelpForRegisteredProcessor(): HelpForRegisteredCommandProcessor

    fun getHelpInGameProcessor(): HelpInGameCommandProcessor

    fun getCreateGameProcessor(): CreateGameCommandProcessor

    fun getJoinGameProcessor(): JoinGameCommandProcessor

    fun getGameFromUnregisteredProcessor(): GameFromUnregisteredCommandProcessor

    fun getExitToMenuProcessor(): ExitToMenuCommandProcessor

    fun getMoveProcessor(): MoveCommandProcessor

    fun getRotateProcessor(): RotateCommandProcessor

    fun getShootProcessor(): ShootCommandProcessor

    fun getObserveGameInfoProcessor(): ObserveGameInfoCommandProcessor

    fun getStopObserveGameInfoProcessor(): StopObserveGameInfoCommandProcessor

    // endregion
}
