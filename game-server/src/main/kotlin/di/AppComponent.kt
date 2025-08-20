package org.example.di

import dagger.Component
import org.example.utils.ServerLooper
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent {

    fun getServerLooper(): ServerLooper
}
