package org.example.utils

import org.example.command.GameCommand
import org.example.contract.ICommand
import org.example.contract.IMovingObject
import ioc.ioc.IScope
import ioc.ioc.IoC
import org.example.model.Message
import org.example.model.Property
import org.example.model.UObject
import org.example.utils.setup.CommandsList

class InterpreterCommand(
    private val message: Message,
    private val baseScope: IScope,
    private val gameThread: ServerThread,
    private val serverThread: ServerThread
) : ICommand {

    override fun invoke() {
        // Game Command or Common Command
        if (message.gameId != null) {
            val game = IoC.resolve("GameCommand.Get", listOf(message.gameId, baseScope)) as GameCommand
            val userName = UsersAccessValidator.getUsername(message)
            println("AAAAOBJECT game = $game")
            val uObject = if (userName != null) {
                game.switchToUserScope(userName)
                IoC.resolve("GameObject.Get", listOf(message.gameObjectId)) as UObject
            } else {
                null
            }
            println("AAAAOBJECT start ${game.getObjectById(3)} as moving: ${game.getObjectById(3)?.getProperty(Property.LOCATION)} and ${game.getObjectById(3)?.getProperty(Property.VELOCITY)}")
            println("AAAAOBJECT uObject = $uObject")
            val command = CommandsList.getCommandById(message.operationId, uObject, gameThread, message.args)
            println("AAAAOBJECT command = $command")
            game.gameQueue.add(command)

            println("AAAAOBJECT end ${game.getObjectById(3)} as moving: ${game.getObjectById(3)?.getProperty(Property.LOCATION)} and ${game.getObjectById(3)?.getProperty(Property.VELOCITY)}")
        } else {
            val command = CommandsList.getCommandById(message.operationId, null, serverThread, message.args)

            serverThread.commandQueue.add(command)
        }
    }
}
