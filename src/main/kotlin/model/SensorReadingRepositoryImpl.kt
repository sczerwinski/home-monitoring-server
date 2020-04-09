package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.SensorReadingDao
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.Period

@Repository
class SensorReadingRepositoryImpl(
    private val sensorReadingDao: SensorReadingDao
) : SensorReadingRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findLatestReading(location: Location, type: SensorType): SensorReading? =
        sensorReadingDao.findFirst1ByLocationAndTypeOrderByDateTimeDesc(
            location = location,
            type = type
        ).orElse(null)

    override fun findByDate(location: Location, type: SensorType, date: LocalDate): List<SensorReading> =
        sensorReadingDao.findByLocationAndTypeAndDateTimeBetweenOrderByDateTime(
            location = location,
            type = type,
            start = date.atStartOfDay(),
            end = (date + Period.ofDays(1)).atStartOfDay()
        ).toList()

    override fun save(sensorReading: SensorReading) {
        val savedReading = sensorReadingDao.save(sensorReading)
        logger.info("Sensor reading created: $savedReading")
    }
}
