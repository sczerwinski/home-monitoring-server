package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Period

class SensorReadingMatchersIsEqualTest {

    @Test
    fun `When the same sensor reading, then matches`() {
        val sensorReading = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = LocalDateTime.now(),
            value = 23.4
        )

        assertThat(sensorReading, SensorReadingMatchers.isEqual(sensorReading))
    }

    @Test
    fun `When different id, then matches`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 456L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )

        assertThat(sensorReading1, SensorReadingMatchers.isEqual(sensorReading2))
    }

    @Test
    fun `When different location id, then matches`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 123L,
            location = Location(id = 2L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )

        assertThat(sensorReading1, SensorReadingMatchers.isEqual(sensorReading2))
    }

    @Test
    fun `When different location name, then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Living room"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )

        assertThat(sensorReading1, IsNot(SensorReadingMatchers.isEqual(sensorReading2)))
    }

    @Test
    fun `When different sensor type, then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.HUMIDITY,
            dateTime = dateTime,
            value = 23.4
        )

        assertThat(sensorReading1, IsNot(SensorReadingMatchers.isEqual(sensorReading2)))
    }

    @Test
    fun `When different date and time, then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime + Period.ofDays(1),
            value = 23.4
        )

        assertThat(sensorReading1, IsNot(SensorReadingMatchers.isEqual(sensorReading2)))
    }

    @Test
    fun `When different reading value, then doesn't match`() {
        val dateTime = LocalDateTime.now()
        val sensorReading1 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.4
        )
        val sensorReading2 = SensorReading(
            id = 123L,
            location = Location(id = 1L, name = "Kitchen"),
            type = SensorType.TEMPERATURE,
            dateTime = dateTime,
            value = 23.5
        )

        assertThat(sensorReading1, IsNot(SensorReadingMatchers.isEqual(sensorReading2)))
    }
}
