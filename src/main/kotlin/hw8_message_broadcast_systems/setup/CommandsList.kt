package org.example.hw8_message_broadcast_systems.setup

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.UObject
import org.example.hw5_ioc.ioc.IoC
import org.example.hw8_message_broadcast_systems.commands.EmptyCommand

object CommandsList {

    private val commands: Map<Long, String> = mapOf(
        1L to "MoveCommand.Get",
        2L to "RotateCommand.Get",
        3L to "HardStopCommand.Get",
        4L to "SoftStopCommand.Get",
    )

    fun getCommandById(id: Long, uObject: UObject, thread: ServerThread, args: Map<String, String?>): ICommand {
        val command = commands[id] ?: return EmptyCommand()

        return IoC.resolve(command, listOf(uObject, thread, args)) as ICommand
    }
}
