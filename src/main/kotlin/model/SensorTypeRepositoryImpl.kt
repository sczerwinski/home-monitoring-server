package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.SensorReadingDao
import org.springframework.stereotype.Repository

@Repository
class SensorTypeRepositoryImpl(
    private val sensorReadingDao: SensorReadingDao
) : SensorTypeRepository {

    override fun findByLocation(location: Location): List<SensorType> =
        sensorReadingDao.findSensorTypesByLocation(
            location = location
        ).toList()
}
