package core.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.apache.kafka.common.errors.NetworkException
import org.example.core.utils.HttpClient
import org.example.core.utils.NetworkClient
import org.example.exceptions.AuthErrorException
import org.example.interpreters.ErrorCodeInterpreter
import org.junit.jupiter.api.Test
import kotlin.test.*

class NetworkClientTest {

    @Test
    fun `WHEN response successful EXPECT get success response`() {
        // Arrange
        val mockedHttpClient: HttpClient = mockk()
        every { mockedHttpClient.makeRequest(REQUEST, PATH) } returns Response.Builder()
            .request(Request.Builder().url("https://example.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), "Test"))
            .build()

        val mockedErrorCodeInterpreter: ErrorCodeInterpreter = mockk()
        every { mockedErrorCodeInterpreter.interpret(403) } returns AuthErrorException()
        val networkClient = NetworkClient(
            mockedHttpClient,
            mockedErrorCodeInterpreter,
        )

        // Act
        val response = networkClient.sendJsonToMiddleware(REQUEST, PATH)

        // Assert
        verify(exactly = 0) { mockedErrorCodeInterpreter.interpret(any()) }
        assertTrue(response.isSuccess)
        assertEquals("Test", response.body)
        assertNull(response.error)
    }

    @Test
    fun `WHEN response error EXPECT get error response`() {
        // Arrange
        val mockedHttpClient: HttpClient = mockk()
        every { mockedHttpClient.makeRequest(REQUEST, PATH) } returns Response.Builder()
            .request(Request.Builder().url("https://example.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(403)
            .message("OK")
            .body(ResponseBody.create("text/plain".toMediaType(), "Test"))
            .build()

        val mockedErrorCodeInterpreter: ErrorCodeInterpreter = mockk()
        every { mockedErrorCodeInterpreter.interpret(403) } returns AuthErrorException()
        val networkClient = NetworkClient(
            mockedHttpClient,
            mockedErrorCodeInterpreter,
        )

        // Act
        val response = networkClient.sendJsonToMiddleware(REQUEST, PATH)

        // Assert
        verify(exactly = 1) { mockedErrorCodeInterpreter.interpret(any()) }
        assertFalse(response.isSuccess)
        assertEquals("Test", response.body)
        assertIs<AuthErrorException>(response.error)
    }

    @Test
    fun `WHEN response throws exception EXPECT get error response`() {
        // Arrange
        val mockedHttpClient: HttpClient = mockk()
        every { mockedHttpClient.makeRequest(REQUEST, PATH) } throws NetworkException()

        val mockedErrorCodeInterpreter: ErrorCodeInterpreter = mockk()
        val networkClient = NetworkClient(
            mockedHttpClient,
            mockedErrorCodeInterpreter,
        )

        // Act
        val response = networkClient.sendJsonToMiddleware(REQUEST, PATH)

        // Assert
        assertFalse(response.isSuccess)
        assertNull(response.body)
        assertIs<NetworkException>(response.error)
    }
}

private const val REQUEST = "request"
private const val PATH = "path"
