package org.example.core.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStateListener @Inject constructor(
    private val kafkaConsumer: KafkaConsumer
) {

    private var thread: Thread? = null

    private var stop = AtomicBoolean(false)

    fun startObserve(gameId: Long) {
        stop.set(false)

        System.setErr(java.io.PrintStream(object : java.io.OutputStream() {
            override fun write(b: Int) {}
        }))

        thread = Thread {
            withKafkaLogMuted {
                kafkaConsumer.instance.subscribe(listOf("game_state"))
            }

            kafkaConsumer.instance.use {
                while (!stop.get()) {
                    val records = kafkaConsumer.instance.poll(Duration.ofMillis(1000))

                    val lastRecord = records.lastOrNull()
                    if (lastRecord != null) {

                        val message = Gson().fromJson(lastRecord.value().toString(), GameObjects::class.java)

                        if (gameId == message.gameId) {

                            val output = StringBuilder()
                            output.append(
                                """
                                    Game state has changed:
                                
                                """.trimIndent()
                            )

                            for (obj in message.gameObjects) {
                                output.append("\n${obj?.username}'s spaceShip position is [${obj?.x}, ${obj?.y}] and direction is ${obj?.angle} degrees")
                            }

                            println(output)
                        }
                    }
                }

                kafkaConsumer.instance.close()
            }
        }
        thread?.start()

        Runtime.getRuntime().addShutdownHook(Thread {
            stop.set(true)
            thread?.join()
        })
    }

    fun stopObserve() {
        stop.set(true)
    }

    private fun withKafkaLogMuted(block: () -> Unit) {
        val kafkaLogger = LoggerFactory.getLogger("org.apache.kafka") as Logger
        kafkaLogger.level = Level.WARN
        block.invoke()
    }
}

data class GameObjects(
    val gameId: Long,
    val gameObjects: List<GameObject?>
)

data class GameObject(
    val username: String,
    val x: Int,
    val y: Int,
    val angle: Int,
)
