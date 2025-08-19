package org.example.command

import org.example.contract.ICommand

class EmptyCommand : ICommand {

    override fun invoke() {
        // do nothing
    }
}
