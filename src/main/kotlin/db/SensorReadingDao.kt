package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorType
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

/**
 * A DAO for [SensorReading]s.
 */
interface SensorReadingDao : CrudRepository<SensorReading, Long> {

    /**
     * Retrieves latest [SensorReading] of given [type] for a given [location].
     */
    fun findFirst1ByLocationAndTypeOrderByDateTimeDesc(
        location: Location,
        type: SensorType
    ): Optional<SensorReading>

    /**
     * Retrieves [SensorReading]s of given [type] for a given [location],
     * recorded between [start] and [end] date and time.
     */
    fun findByLocationAndTypeAndDateTimeBetweenOrderByDateTime(
        location: Location,
        type: SensorType,
        start: LocalDateTime,
        end: LocalDateTime
    ): Iterable<SensorReading>

    /**
     * Retrieves [SensorType]s for a given [location].
     */
    fun findSensorTypesByLocation(
        location: Location
    ): Iterable<SensorType>
}
