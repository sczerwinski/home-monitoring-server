package it.czerwinski.home.monitoring.model

import it.czerwinski.home.monitoring.db.LocationDao
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class LocationRepositoryImpl(
    private val locationDao: LocationDao
) : LocationRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun findAll(): List<Location> =
        locationDao.findAll().toList()

    override fun findByName(name: String): Location? =
        locationDao.findByName(name)
            .orElseGet {
                logger.warn("Location \"$name\" doesn't exist")
                null
            }

    override fun findByNameOrCreate(name: String): Location =
        locationDao.findByName(name)
            .orElseGet {
                logger.warn("Location \"$name\" doesn't exist")
                locationDao.save(Location(name = name))
                    .also { savedLocation ->
                        logger.info("Location created: $savedLocation")
                    }
            }
}
