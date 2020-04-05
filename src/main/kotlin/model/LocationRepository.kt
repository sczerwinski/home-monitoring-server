package it.czerwinski.home.monitoring.model

/**
 * A repository of [Location]s.
 */
interface LocationRepository {

    /**
     * Returns all existing [Location]s.
     */
    fun findAll(): List<Location>

    /**
     * Retrieves a [Location] by [name].
     */
    fun findByName(name: String): Location?

    /**
     * Retrieves a [Location] by [name].
     *
     * If no location with a given [name] was found, a new [Location] is created.
     */
    fun findByNameOrCreate(name: String): Location
}
