package org.example.hw8_message_broadcast_systems.commands

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import java.awt.Point
import java.util.LinkedList
import java.util.Queue

class GameCommand(
    private val scope: IScope?,
    val gameQueue: Queue<ICommand?> = LinkedList(),
    private val quant: Long = DEFAULT_QUANT,
) : ICommand {

    // example object
     val objects: MutableMap<Long, UObject> = mutableMapOf(
        3L to SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
            setProperty(Property.LOCATION, Point(12, 5))
        }
    )

    override fun invoke() {
        IoC.resolve("Scopes.Current.Set", scope)

        val startTime = System.currentTimeMillis()
        while (startTime + quant > System.currentTimeMillis()) {
            val command = gameQueue.poll()

            try {
                command?.invoke()
            } catch (e: Exception) {
                (IoC.resolve("ExceptionHandler", listOf(command, e)) as ICommand).invoke()
            }
        }
    }

    fun getObjectById(id: Long, args: Map<String, String?>): UObject {
        val uObject = objects[id]
        return if (uObject == null) {
            val newObject = IoC.resolve("UObject.Get", args) as UObject
            objects[id] = newObject
            newObject
        } else {
            uObject
        }
    }

    fun hasWork(): Boolean = gameQueue.isNotEmpty()
}

private const val DEFAULT_QUANT = 20L
