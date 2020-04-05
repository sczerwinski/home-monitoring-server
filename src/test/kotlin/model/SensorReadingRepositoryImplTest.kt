package it.czerwinski.home.monitoring.model

import com.ninjasquad.springmockk.MockkBean
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import it.czerwinski.home.monitoring.db.SensorReadingDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class SensorReadingRepositoryImplTest {

    @MockkBean
    lateinit var sensorReadingDao: SensorReadingDao

    @Autowired
    lateinit var sensorReadingRepository: SensorReadingRepository

    @Test
    fun `When findLatestReading, then return latest sensor reading`() {
        val location = Location(id = 1L, name = "Kitchen")
        val reading = SensorReading(
            id = 123L,
            location = location,
            type = SensorType.TEMPERATURE,
            dateTime = LocalDateTime.now(),
            value = 23.4
        )
        every {
            sensorReadingDao.findFirst1ByLocationAndTypeOrderByDateTimeDesc(
                location = location,
                type = SensorType.TEMPERATURE
            )
        } returns Optional.of(reading)

        val result = sensorReadingRepository.findLatestReading(
            location = location,
            type = SensorType.TEMPERATURE
        )

        assertTrue(result.isPresent)
        assertEquals(reading, result.get())
    }

    @Test
    fun `When findByDate, then return sensor readings for the whole day`() {
        val date = LocalDate.of(2020, 1, 1)
        val location = Location(id = 1L, name = "Kitchen")
        val readings = listOf(
            SensorReading(
                id = 123L,
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 0),
                value = 23.4
            ),
            SensorReading(
                id = 123L,
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(13, 0),
                value = 23.5
            )
        )
        every {
            sensorReadingDao.findByLocationAndTypeAndDateTimeBetween(
                location = location,
                type = SensorType.TEMPERATURE,
                start = LocalDateTime.of(2020, 1, 1, 0, 0),
                end = LocalDateTime.of(2020, 1, 2, 0, 0)
            )
        } returns readings

        val result = sensorReadingRepository.findByDate(
            location = location,
            type = SensorType.TEMPERATURE,
            date = date
        )

        assertEquals(readings, result)
    }

    @Test
    fun `When save, then save new sensor reading`() {
        val location = Location(id = 1L, name = "Kitchen")
        val reading = SensorReading(
            location = location,
            type = SensorType.TEMPERATURE,
            dateTime = LocalDateTime.now(),
            value = 23.4
        )
        every { sensorReadingDao.save(reading) } returnsArgument 0

        sensorReadingRepository.save(sensorReading = reading)

        verify(exactly = 1) { sensorReadingDao.save(reading) }
        confirmVerified(sensorReadingDao)
    }
}
