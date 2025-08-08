package org.example.hw8_message_broadcast_systems.commands

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.utils.ILambda
import org.jetbrains.annotations.TestOnly
import java.awt.Point
import java.util.LinkedList
import java.util.Queue

class GameCommand(
    private val gameScope: IScope?,
    val gameQueue: Queue<ICommand?> = LinkedList(),
    private val quant: Long = DEFAULT_QUANT,
) : ICommand {

    // example object
    private val objects: MutableMap<Long, UObject> = mutableMapOf(
        3L to SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
            setProperty(Property.LOCATION, Point(12, 5))
        }
    )

    // example object 2
    private val objects2: MutableMap<Long, UObject> = mutableMapOf(
        3L to SpaceShip().apply {
            setProperty(Property.VELOCITY, 8)
            setProperty(Property.ANGLE, Angle(3, 7))
            setProperty(Property.LOCATION, Point(12, 5))
        }
    )

    val userScopes: MutableMap<String, IScope> = mutableMapOf(

    )

    val userObjects: MutableMap<String, MutableMap<Long, UObject>> = mutableMapOf(
        "agent1" to objects,
        "agent2" to objects2,
    )

    override fun invoke() {
        IoC.resolve("Scopes.Current.Set", gameScope)

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

    fun getObjectById(id: Long?, args: Map<String, String?>): UObject? {
        if (id == null) return null

        val uObject = objects[id]
        return if (uObject == null) {
            val newObject = IoC.resolve("UObject.Get", args) as UObject
            objects[id] = newObject
            newObject
        } else {
            uObject
        }
    }

    fun switchToUserScope(username: String?) {
        if (username == null) return

        val scope = userScopes[username]
        if (scope == null) {
            val newScope = IoC.resolve("Scopes.New", gameScope) as IScope
            userScopes[username] = newScope
            IoC.resolve("Scopes.Current.Set", newScope)
            if (userObjects[username] == null) {
                userObjects[username] = mutableMapOf()
            }

            IoC.resolve(
                "IoC.Register",
                listOf(
                    "GameObject.Get",
                    ILambda { _, params ->
                        val objectId = params?.get(0) as Long

                        val objects = userObjects[username]
                        objects?.get(objectId)
                    }
                )
            )
        } else {
            IoC.resolve("Scopes.Current.Set", scope)
        }
    }

    fun hasWork(): Boolean = gameQueue.isNotEmpty()

    @TestOnly
    fun getObjectById(objId: Long): UObject? {
        return objects[objId]
    }

    @TestOnly
    fun getObject2ById(objId: Long): UObject? {
        return objects2[objId]
    }
}

private const val DEFAULT_QUANT = 20L
