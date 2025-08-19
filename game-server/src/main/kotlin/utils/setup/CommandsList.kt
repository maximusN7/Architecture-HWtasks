package org.example.utils.setup

import org.example.command.EmptyCommand
import org.example.contract.ICommand
import ioc.ioc.IoC
import org.example.model.UObject
import org.example.utils.ServerThread

object CommandsList {

    private val commands: Map<Long, String> = mapOf(
        1L to "MoveCommand.Get",
        2L to "RotateCommand.Get",
        3L to "ShootCommand.Get",
    )

    fun getCommandById(id: Long, uObject: UObject?, thread: ServerThread, args: Map<String, String?>): ICommand {
        val command = commands[id] ?: return EmptyCommand()

        return IoC.resolve(command, listOf(uObject, thread, args)) as ICommand
    }
}
