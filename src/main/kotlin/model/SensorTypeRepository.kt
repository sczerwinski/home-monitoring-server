package it.czerwinski.home.monitoring.model

/**
 * A repository of [SensorType]s.
 */
interface SensorTypeRepository {

    /**
     * Retrieves all [SensorType]s with recorded readings for a given [location].
     */
    fun findByLocation(location: Location): List<SensorType>
}
