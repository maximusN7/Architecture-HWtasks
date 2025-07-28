package org.example.hw12_chain_of_responsibility_pattern

import org.example.hw2_exceptionhandler.contract.ICommand
import org.example.hw4_command.command.MacroCommand
import java.awt.Point
import java.util.*
import kotlin.math.floor

class CheckRegionCommand(
    val regionsSystem: RegionSystem,
    val currentObject: IGameFieldObject,
    val isPointObject: Boolean = true,
) : ICommand {

    override fun invoke() {
        val objPosition = currentObject.getLocation()

        // find coordinates of the region for the object
        val x1 = floor(objPosition.x.toDouble() / regionsSystem.regionSize).toInt() * regionsSystem.regionSize + regionsSystem.pointOfMeasureShift
        val y1 = floor(objPosition.y.toDouble() / regionsSystem.regionSize).toInt() * regionsSystem.regionSize + regionsSystem.pointOfMeasureShift
        val x2 = x1 + regionsSystem.regionSize
        val y2 = y1 + regionsSystem.regionSize

        val currentRegion = Region(Point(x1, y1), Point(x2, y2))

        regionsSystem.putObjectInRegion(currentObject, currentRegion)

        val commands = LinkedList<ICommand>()
        val objectsInRegion = regionsSystem.regionsAndObjects[currentRegion]
        objectsInRegion?.forEach {
            if (it != currentObject) {
                if (isPointObject) {
                    commands.add(CheckCollisionForPointCommand(currentObject, it))
                } else {
                    commands.add(CheckCollisionForThreeDotObjectCommand(currentObject, it))
                }
            }
        }
        MacroCommand(commands).invoke()
    }
}
