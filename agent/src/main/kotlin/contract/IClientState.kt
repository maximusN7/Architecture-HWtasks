package org.example.contract

interface IClientState {

    fun handle(command: ICommand?): IClientState?
}
