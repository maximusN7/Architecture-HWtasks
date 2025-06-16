package org.example.hw8_message_broadcast_systems.setup

import hw7_vertical_scaling_and_synchronization.ServerThread
import hw7_vertical_scaling_and_synchronization.commands.HardStopCommand
import hw7_vertical_scaling_and_synchronization.commands.SoftStopCommand
import org.example.hw2_exceptionhandler.ExceptionHandler
import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.SpaceShip
import org.example.hw3_abstractions.UObject
import org.example.hw3_abstractions.command.MoveCommand
import org.example.hw3_abstractions.command.RotateCommand
import org.example.hw3_abstractions.contract.IMovingObject
import org.example.hw3_abstractions.contract.IRotatingObject
import org.example.hw3_abstractions.model.Angle
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.ioc.command.InitScopeBasedIoCCommand
import org.example.hw5_ioc.utils.ILambda
import org.example.hw6_adapter_and_bridge.ioc.generating.CreateInterfaceAdapterStrategy
import org.example.hw6_adapter_and_bridge.ioc.generating.utils.AdapterFactoryNotExistingException
import org.example.hw8_message_broadcast_systems.GameScheduler
import java.awt.Point
import kotlin.reflect.KClass

object InitEndpointIoC {

    fun invoke() {
        InitScopeBasedIoCCommand(
            defaultMissingStrategy = { _, _ -> throw AdapterFactoryNotExistingException() }
        ).invoke()

        IoC.resolve(
            "IoC.Register",
            listOf(
                "Adapter",
                ILambda { _, params ->
                    CreateInterfaceAdapterStrategy(
                        params?.get(0) as KClass<out Any>,
                        params[1] as UObject
                    ).resolve()
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "ExceptionHandler",
                ILambda { _, params ->
                    ExceptionHandler.handle(params?.get(0) as ICommand?, params?.get(1) as Exception)
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "GameCommand.Get",
                ILambda { _, params ->
                    GameScheduler.getGameById(params?.get(0) as Long, params?.get(1) as IScope)
                }
            )
        )

        initBaseCommands()
        initUObjectsGetter()
    }

    private fun initBaseCommands() {
        IoC.resolve(
            "IoC.Register",
            listOf(
                "MoveCommand.Get",
                ILambda { _, params ->
                    val uObject = params?.get(0) as UObject
                    val args = params?.get(2) as? Map<String, String?>

                    args?.get("velocity")?.let {
                        uObject.setProperty(Property.VELOCITY, it.toInt())
                    }

                    val adapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))

                    MoveCommand(adapter as IMovingObject)
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "RotateCommand.Get",
                ILambda { _, params ->
                    val uObject = params?.get(0) as UObject
                    val args = params?.get(2)

                    val adapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))
                    RotateCommand(adapter as IRotatingObject)
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "HardStopCommand.Get",
                ILambda { _, params ->
                    val args = params?.get(2)

                    HardStopCommand(params?.get(1) as ServerThread)
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "SoftStopCommand.Get",
                ILambda { _, params ->
                    val args = params?.get(2)

                    SoftStopCommand(params?.get(1) as ServerThread)
                }
            )
        )
    }

    private fun initUObjectsGetter() {
        // Based on params we can get different UObjects. Now it is only SpaceShip
        IoC.resolve(
            "IoC.Register",
            listOf(
                "UObject.Get",
                ILambda { _, params ->
                    val args = params?.get(0)

                    SpaceShip().apply {
                        setProperty(Property.VELOCITY, 8)
                        setProperty(Property.ANGLE, Angle(3, 7))
                        setProperty(Property.LOCATION, Point(12, 5))
                    }
                }
            )
        )
    }
}
