import org.example.QuadraticEquation
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class QuadraticEquationTest {

    private val quadraticEquation: QuadraticEquation = QuadraticEquation()

    @Test
    fun WHEN_discriminant_is_less_than_zero_EXPECT_no_answers() {
        // Act
        val result = quadraticEquation.solve(1.0, 0.0, 1.0)

        // Assert
        assert(result.isEmpty())
    }

    @Test
    fun WHEN_discriminant_is_more_than_zero_EXPECT_two_answers() {
        // Act
        val result = quadraticEquation.solve(1.0, 0.0, -1.0)

        // Assert
        assert(result.size == 2)
        assert(result.contains(1.0))
        assert(result.contains(-1.0))
    }

    @Test
    fun WHEN_discriminant_is_equals_to_zero_EXPECT_one_answer() {
        // Act
        val result = quadraticEquation.solve(1.0, 1e-5, 1e-10 / 4)

        // Assert
        assert(result.size == 2)
        assert(result[0] == -1e-5 / 2)
        assert(result[1] == -1e-5 / 2)
    }

    @Test
    fun WHEN_a_equals_to_zero_EXPECT_exception() {
        // Act
        val result = kotlin.runCatching {
            quadraticEquation.solve(0.0, 2.0, 1.0)
        }

        // Assert
        assert(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
        assertEquals("'a' is required to be not zero", result.exceptionOrNull()?.message)
    }

    @Test
    fun WHEN_one_of_the_arguments_is_NaN_EXPECT_exception() {
        // Act
        val result = kotlin.runCatching {
            quadraticEquation.solve(Double.NaN, Double.NaN, Double.NaN)
        }

        // Assert
        assert(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
        assertEquals("one of the arguments is NaN", result.exceptionOrNull()?.message)
    }

    @Test
    fun WHEN_one_of_the_arguments_is_negative_infinity_EXPECT_exception() {
        // Act
        val result = kotlin.runCatching {
            quadraticEquation.solve(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        }

        // Assert
        assert(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
        assertEquals("one of the arguments is Infinite", result.exceptionOrNull()?.message)
    }

    @Test
    fun WHEN_one_of_the_arguments_is_positive_infinity_EXPECT_exception() {
        // Act
        val result = kotlin.runCatching {
            quadraticEquation.solve(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        }

        // Assert
        assert(result.isFailure)
        assertIs<IllegalArgumentException>(result.exceptionOrNull())
        assertEquals("one of the arguments is Infinite", result.exceptionOrNull()?.message)
    }
}
