package org.example.hw8_message_broadcast_systems.commands

import org.example.hw2_exceptionhandler.contract.ICommand

class EmptyCommand : ICommand {

    override fun invoke() {
        // do nothing
    }
}
