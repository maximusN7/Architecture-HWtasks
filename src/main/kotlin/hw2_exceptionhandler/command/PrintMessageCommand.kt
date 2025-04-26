package org.example.hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.contract.ICommand

class PrintMessageCommand(
    private val message: String,
) : ICommand {

    override fun invoke() {
        println("incoming message: $message")
    }
}
