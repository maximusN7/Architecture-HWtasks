package org.example.command

import org.example.contract.ICommand
import ioc.ioc.IScope
import ioc.ioc.IoC
import ioc.utils.ILambda
import org.example.model.Angle
import org.example.model.Property
import org.example.model.SpaceShip
import org.example.model.UObject
import org.example.utils.GameStateNotifier
import org.example.utils.KafkaMessage
import org.example.utils.KafkaProducer
import org.example.utils.SpaceSheepFactory
import org.jetbrains.annotations.TestOnly
import java.awt.Point
import java.util.*

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
            setProperty(Property.LOCATION, Point(0, 0))
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
                        val obj = objects?.get(objectId)
                        val resultObj = if (obj == null) {
                            val newObj = SpaceSheepFactory().getSpaceSheep(Point(0, 0))
                            objects?.put(objectId, newObj)
                            newObj
                        } else {
                            obj
                        }

                        resultObj
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
