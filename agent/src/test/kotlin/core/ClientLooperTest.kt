package core

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.example.contract.IClientState
import org.example.core.ClientLooper
import org.example.core.utils.ConsoleCommandProducer
import org.example.core.utils.ConsoleStringCommand
import org.junit.jupiter.api.Test

class ClientLooperTest {

    @Test
    fun `WHEN command is exit EXPECT loop brakes`() {
        // Arrange
        val mockedCommandProducer: ConsoleCommandProducer = mockk()
        val mockedCommand: ConsoleStringCommand = mockk()
        every { mockedCommandProducer.getCommand() } returns mockedCommand
        val mockedState: IClientState = mockk()
        every { mockedState.handle(mockedCommand) } returns null
        val looper = spyk(ClientLooper(mockedCommandProducer))
        looper::class.java.getDeclaredField("state").apply {
            isAccessible = true
            set(looper, mockedState)
        }

        // Act
        looper.startProcess()

        // Assert
        verify(exactly = 1) { mockedCommandProducer.getCommand() }
        verify(exactly = 1) { mockedState.handle(mockedCommand) }
    }
}
