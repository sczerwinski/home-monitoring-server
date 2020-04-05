package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.model.Location
import org.springframework.data.repository.CrudRepository

/**
 * A DAO for [Location]s.
 */
interface LocationDao : CrudRepository<Location, Long>
