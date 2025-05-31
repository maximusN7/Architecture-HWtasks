package hw7_vertical_scaling_and_synchronization.commands

import hw7_vertical_scaling_and_synchronization.ServerThread
import org.example.hw2_exceptionhandler.contract.ICommand

class HardStopCommand(
    private val thread: ServerThread
) : ICommand {

    override fun invoke() {
        thread.stop()
    }
}
