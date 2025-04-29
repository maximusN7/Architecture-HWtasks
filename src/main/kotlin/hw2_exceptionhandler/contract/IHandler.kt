package org.example.hw2_exceptionhandler.contract

interface IHandler {

    fun handle(command: ICommand, exception: Exception): ICommand
}
