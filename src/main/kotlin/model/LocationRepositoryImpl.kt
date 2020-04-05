package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.LocationDao
import org.springframework.stereotype.Repository

@Repository
class LocationRepositoryImpl(
    private val locationDao: LocationDao
) : LocationRepository {

    override fun findAll(): List<Location> =
        locationDao.findAll().toList()
}
