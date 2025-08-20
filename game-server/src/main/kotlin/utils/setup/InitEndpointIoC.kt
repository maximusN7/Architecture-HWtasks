package org.example.utils.setup

import org.example.command.MoveCommand
import org.example.command.RotateCommand
import org.example.command.ShootCommand
import org.example.contract.ICommand
import org.example.contract.IMovingObject
import org.example.contract.IRotatingObject
import org.example.contract.IShootingObject
import ioc.ioc.IScope
import ioc.ioc.IoC
import org.example.hw5_ioc.ioc.command.InitScopeBasedIoCCommand
import ioc.utils.ILambda
import org.example.model.Property
import org.example.model.UObject
import org.example.utils.GameScheduler
import org.example.utils.GetCommandAdapter
import kotlin.reflect.KClass

object InitEndpointIoC {

    fun invoke() {
        InitScopeBasedIoCCommand().invoke()

        IoC.resolve(
            "IoC.Register",
            listOf(
                "Adapter",
                ILambda { _, params ->
                    GetCommandAdapter(
                        params?.get(0) as KClass<out Any>,
                        params[1] as UObject
                    ).getAdapter()
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
                    println("MoveCommand.Get 1")
                    val uObject = params?.get(0) as UObject
                    val args = params?.get(2) as? Map<String, String?>
                    println("MoveCommand.Get 2")
                    args?.get("velocity")?.let {
                        uObject.setProperty(Property.VELOCITY, it.toInt())
                    }
                    println("MoveCommand.Get 3")
                    val adapter = IoC.resolve("Adapter", listOf(IMovingObject::class, uObject))
                    println("MoveCommand.Get 4")
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
                    val args = params?.get(2) as? Map<String, String?>

                    args?.get("angularVelocity")?.let {
                        uObject.setProperty(Property.ANGULAR_VELOCITY, it.toByte())
                    }

                    val adapter = IoC.resolve("Adapter", listOf(IRotatingObject::class, uObject))
                    RotateCommand(adapter as IRotatingObject)
                }
            )
        )

        IoC.resolve(
            "IoC.Register",
            listOf(
                "ShootCommand.Get",
                ILambda { _, params ->
                    val uObject = params?.get(0) as UObject

                    val adapter = IoC.resolve("Adapter", listOf(IShootingObject::class, uObject))

                    ShootCommand(adapter as IShootingObject)
                }
            )
        )
    }

    private fun initUObjectsGetter() {
        // Based on params we can get different UObjects. Now it is only SpaceShip
//        IoC.resolve(
//            "IoC.Register",
//            listOf(
//                "UObject.Get",
//                ILambda { _, params ->
//                    val args = params?.get(0)
//
//                    SpaceShip().apply {
//                        setProperty(Property.VELOCITY, 8)
//                        setProperty(Property.ANGLE, Angle(3, 7))
//                        setProperty(Property.LOCATION, Point(12, 5))
//                    }
//                }
//            )
//        )
    }
}
