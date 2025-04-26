package org.example.hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.contract.ICommand

class LogExceptionCommand(
    private val command: ICommand,
    private val exception: Exception,
) : ICommand {

    override fun invoke() {
        println("during proceeding of ${command::class.simpleName} occurred exception $exception")
    }
}
