package it.czerwinski.home.monitoring.model

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.czerwinski.home.monitoring.db.SensorReadingDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SensorTypeRepositoryImplTest {

    @MockkBean
    lateinit var sensorReadingDao: SensorReadingDao

    @Autowired
    lateinit var sensorTypeRepository: SensorTypeRepository

    @Test
    fun `When findSensorTypesByLocation, then return list of sensor types`() {
        val location = Location(id = 1L, name = "Kitchen")
        every {
            sensorReadingDao.findSensorTypesByLocation(location = location)
        } returns listOf(SensorType.TEMPERATURE, SensorType.HUMIDITY)

        val result = sensorTypeRepository.findByLocation(location = location)

        assertEquals(listOf(SensorType.TEMPERATURE, SensorType.HUMIDITY), result)
    }
}
