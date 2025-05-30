package hw7_vertical_scaling_and_synchronization.commands

import org.example.hw2_exceptionhandler.contract.ICommand
import java.util.concurrent.CountDownLatch

class SynchronizationCommand(
    private val startLatch: CountDownLatch,
    private val endLatch: CountDownLatch,
) : ICommand {

    override fun invoke() {
        startLatch.countDown()
        endLatch.await()
    }
}
