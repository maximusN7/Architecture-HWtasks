package org.example.interpreters

import javax.inject.Inject

class GameCommandInterpreter @Inject constructor() {

    fun interpret(command: GamePlayCommand): Long {
        return when (command) {
            GamePlayCommand.MOVE -> 1L
            GamePlayCommand.ROTATE -> 2L
            GamePlayCommand.SHOOT -> 3L
        }
    }
}

enum class GamePlayCommand {

    MOVE, ROTATE, SHOOT,
}
