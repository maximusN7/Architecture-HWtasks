package org.example.hw12_chain_of_responsibility_pattern

import org.example.hw2_exceptionhandler.contract.ICommand

class CheckCollisionForPointCommand(
    private val currentObject: IGameFieldObject,
    private val anotherObject: IGameFieldObject,
) : ICommand {

    override fun invoke() {
        if (CollisionChecker.checkIsCollision(currentObject, anotherObject)) {
            currentObject.setIsCollided(true)
            anotherObject.setIsCollided(true)
        }
    }
}
