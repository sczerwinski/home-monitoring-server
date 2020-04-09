package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.matchers.SensorReadingMatchers
import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsArray
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDate

@DataJpaTest
class SensorReadingDaoTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val sensorReadingDao: SensorReadingDao
) {

    @Test
    fun `When findFirst1ByLocationAndTypeOrderByDateTimeDesc, then return latest reading of type for location`() {
        val date = LocalDate.of(2020, 1, 1)
        val locations = listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .map { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        val readings = listOf(
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 9),
                value = 15.0
            ),
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 10),
                value = 20.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 11),
                value = 25.0
            ),
            SensorReading(
                location = locations[0],
                type = SensorType.HUMIDITY,
                dateTime = date.atTime(12, 12),
                value = 40.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.HUMIDITY,
                dateTime = date.atTime(12, 13),
                value = 50.0
            )
        )
        for (reading in readings) {
            entityManager.persist(reading)
        }
        entityManager.flush()

        val result = sensorReadingDao.findFirst1ByLocationAndTypeOrderByDateTimeDesc(
            location = locations[0],
            type = SensorType.TEMPERATURE
        )

        assertTrue(result.isPresent)
        assertThat(result.get(), SensorReadingMatchers.isEqual(readings[1]))
    }

    @Test
    fun `When findByLocationAndTypeAndDateTimeBetween, then return all readings of type for location in time period`() {
        val date = LocalDate.of(2020, 1, 1)
        val locations = listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .map { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        val readings = listOf(
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(11, 50),
                value = 15.0
            ),
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 0),
                value = 15.0
            ),
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 10),
                value = 20.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 11),
                value = 25.0
            ),
            SensorReading(
                location = locations[0],
                type = SensorType.HUMIDITY,
                dateTime = date.atTime(12, 12),
                value = 40.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.HUMIDITY,
                dateTime = date.atTime(12, 13),
                value = 50.0
            )
        )
        for (reading in readings) {
            entityManager.persist(reading)
        }
        entityManager.flush()

        val result = sensorReadingDao.findByLocationAndTypeAndDateTimeBetweenOrderByDateTime(
            location = locations[0],
            type = SensorType.TEMPERATURE,
            start = date.atTime(12, 0),
            end = date.atTime(13, 0)
        )

        assertThat(
            result.toList().toTypedArray(),
            IsArray.array(
                SensorReadingMatchers.isEqual(readings[1]),
                SensorReadingMatchers.isEqual(readings[2])
            )
        )
    }

    @Test
    fun `When findSensorTypesByLocation, then return sensor types for given location`() {
        val date = LocalDate.of(2020, 1, 1)
        val locations = listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .map { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        val readings = listOf(
            SensorReading(
                location = locations[0],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 10),
                value = 20.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.TEMPERATURE,
                dateTime = date.atTime(12, 11),
                value = 25.0
            ),
            SensorReading(
                location = locations[1],
                type = SensorType.HUMIDITY,
                dateTime = date.atTime(12, 13),
                value = 50.0
            )
        )
        for (reading in readings) {
            entityManager.persist(reading)
        }
        entityManager.flush()

        val result = sensorReadingDao.findSensorTypesByLocation(
            location = locations[0]
        )

        assertThat(
            result.toList().toTypedArray(),
            IsArray.array(IsEqual.equalTo(SensorType.TEMPERATURE))
        )
    }
}
