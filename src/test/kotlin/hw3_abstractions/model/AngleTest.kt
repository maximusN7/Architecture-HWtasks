package hw3_abstractions.model

import org.example.hw3_abstractions.model.Angle
import org.junit.jupiter.api.Test
import kotlin.math.cos
import kotlin.test.assertEquals

class AngleTest {

    @Test
    fun WHEN_angle_is_1_of_6_EXPECT_cos_be_1_div_2() {
        // Arrange
        val angle = Angle(1, 6)

        // Act
        val angleRadians = angle.getAngleRadians()
        val cos = cos(angleRadians)
        // Assert
        assertEquals(0.5, cos, TOLERANCE)
    }
}

private const val TOLERANCE = 0.000001
