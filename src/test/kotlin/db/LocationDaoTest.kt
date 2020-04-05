package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.matchers.LocationMatchers
import it.czerwinski.home.monitoring.model.Location
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class LocationDaoTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val locationDao: LocationDao
) {

    @Test
    fun `Given location exists, when findByName, then return location with a given name`() {
        listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .forEach { location -> entityManager.persist(location) }
        entityManager.flush()

        val result = locationDao.findByName(name = "Kitchen")

        assertTrue(result.isPresent)
        assertThat(result.get(), LocationMatchers.isEqual(locationName = "Kitchen"))
    }

    @Test
    fun `Given location doesn't exist, when findByName, then return nothing`() {
        listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .forEach { location -> entityManager.persist(location) }
        entityManager.flush()

        val result = locationDao.findByName(name = "Bedroom")

        assertFalse(result.isPresent)
    }
}
