package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.LocationDao
import org.springframework.stereotype.Repository

@Repository
class LocationRepositoryImpl(
    private val locationDao: LocationDao
) : LocationRepository {

    override fun findAll(): List<Location> =
        locationDao.findAll().toList()

    override fun findByName(name: String): Location? =
        locationDao.findByName(name).orElse(null)

    override fun findByNameOrCreate(name: String): Location =
        locationDao.findByName(name)
            .orElseGet { locationDao.save(Location(name = name)) }
}
