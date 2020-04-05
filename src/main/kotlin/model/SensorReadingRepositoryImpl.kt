package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.SensorReadingDao
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.Period
import java.util.*

@Repository
class SensorReadingRepositoryImpl(
    private val sensorReadingDao: SensorReadingDao
) : SensorReadingRepository {

    override fun findLatestReading(location: Location, type: SensorType): SensorReading? =
        sensorReadingDao.findFirst1ByLocationAndTypeOrderByDateTimeDesc(
            location = location,
            type = type
        ).orElse(null)

    override fun findByDate(location: Location, type: SensorType, date: LocalDate): List<SensorReading> =
        sensorReadingDao.findByLocationAndTypeAndDateTimeBetween(
            location = location,
            type = type,
            start = date.atStartOfDay(),
            end = (date + Period.ofDays(1)).atStartOfDay()
        ).toList()

    override fun save(sensorReading: SensorReading) {
        sensorReadingDao.save(sensorReading)
    }
}
