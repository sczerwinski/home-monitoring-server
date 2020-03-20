package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Temperature
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Period

class TemperatureMatchersIsEqualTest {

    @Test
    fun `When the same temperature then matches`() {
        val temperature = Temperature(
            id = 123L,
            time = LocalDateTime.now(),
            temperature = 12.3
        )

        assertThat(temperature, TemperatureMatchers.isEqual(temperature))
    }

    @Test
    fun `When different id then matches`() {
        val dateTime = LocalDateTime.now()
        val temperature1 = Temperature(
            id = 123L,
            time = dateTime,
            temperature = 12.3
        )
        val temperature2 = Temperature(
            id = 456L,
            time = dateTime,
            temperature = 12.3
        )

        assertThat(temperature1, TemperatureMatchers.isEqual(temperature2))
    }

    @Test
    fun `When different time then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val temperature1 = Temperature(
            id = 123L,
            time = dateTime,
            temperature = 12.3
        )
        val temperature2 = Temperature(
            id = 123L,
            time = dateTime + Period.ofDays(1),
            temperature = 12.3
        )

        assertThat(temperature1, IsNot(TemperatureMatchers.isEqual(temperature2)))
    }

    @Test
    fun `When different temperature then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val temperature1 = Temperature(
            id = 123L,
            time = dateTime,
            temperature = 12.3
        )
        val temperature2 = Temperature(
            id = 123L,
            time = dateTime,
            temperature = 45.6
        )

        assertThat(temperature1, IsNot(TemperatureMatchers.isEqual(temperature2)))
    }
}
