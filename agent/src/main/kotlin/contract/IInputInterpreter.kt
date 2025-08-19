package org.example.contract

interface IInputInterpreter {

    fun interpret(command: String): IClientState?
}
