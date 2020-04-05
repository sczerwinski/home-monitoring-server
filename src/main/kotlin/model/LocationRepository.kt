package it.czerwinski.home.monitoring.model

/**
 * A repository of [Location]s.
 */
interface LocationRepository {

    /**
     * Returns all existing [Location]s.
     */
    fun findAll(): List<Location>
}
