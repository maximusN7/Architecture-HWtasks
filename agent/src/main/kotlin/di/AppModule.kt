package org.example.di

import dagger.Binds
import dagger.Module
import org.example.core.utils.ConsoleCommandProducer
import org.example.contract.ICommandProducer

@Module
interface AppModule {

    @Binds
    fun bindConsoleCommandProducer(commandProducer: ConsoleCommandProducer): ICommandProducer
}
