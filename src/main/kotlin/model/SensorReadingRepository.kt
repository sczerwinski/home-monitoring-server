package it.czerwinski.home.monitoring.model

import java.time.LocalDate
import java.util.*

/**
 * A repository of [SensorReading]s.
 */
interface SensorReadingRepository {

    /**
     * Retrieves the latest [SensorReading] of given [type] for a given [location].
     */
    fun findLatestReading(location: Location, type: SensorType): Optional<SensorReading>

    /**
     * Retrieves all [SensorReading]s of given [type] for a given [location],
     * recorded on a given [date].
     */
    fun findByDate(location: Location, type: SensorType, date: LocalDate): List<SensorReading>

    /**
     * Saves a given [sensorReading].
     */
    fun save(sensorReading: SensorReading)
}
