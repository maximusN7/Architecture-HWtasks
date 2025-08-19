package org.example.command

import org.example.contract.ICommand
import org.example.contract.IShootingObject

class ShootCommand(
    private val shootingObject: IShootingObject
) : ICommand {

    override fun invoke() {
        println("SHOOT")
    }
}
