package org.example.contract

interface ICommandProcessor {

    fun process(currentState: IClientState?, parts: List<String>): IClientState?
}
