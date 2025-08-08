package org.example.hw13_interpreter_pattern

import org.example.hw2_exceptionhandler.contract.ICommand

class ShootCommand(
    private val shootingObject: IShootingObject
) : ICommand {

    override fun invoke() {
        println("SHOOT")
    }
}
