package org.example.utils.setup

import org.example.contract.ICommand

class LogExceptionCommand(
    private val command: ICommand?,
    private val exception: Exception,
) : ICommand {

    override fun invoke() {
        if (command != null) {
            println("during proceeding of ${command::class.simpleName} occurred exception $exception")
        } else {
            println("during looping occurred exception $exception")
        }
    }
}
