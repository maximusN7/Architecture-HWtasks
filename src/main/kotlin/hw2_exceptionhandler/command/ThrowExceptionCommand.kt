package org.example.hw2_exceptionhandler.command

import org.example.hw2_exceptionhandler.contract.ICommand

class ThrowExceptionCommand(
    private val exception: Exception,
) : ICommand {

    override fun invoke() {
        println("throw exception ${exception::class.simpleName}")
        throw exception
    }
}
