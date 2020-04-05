package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.model.Location
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * A DAO for [Location]s.
 */
interface LocationDao : CrudRepository<Location, Long> {

    /**
     * Retrieves a [Location] by its [name].
     */
    fun findByName(name: String): Optional<Location>
}
