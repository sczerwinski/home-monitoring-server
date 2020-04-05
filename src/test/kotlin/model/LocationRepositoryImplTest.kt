package it.czerwinski.home.monitoring.model

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Called
import io.mockk.every
import io.mockk.verify
import it.czerwinski.home.monitoring.db.LocationDao
import it.czerwinski.home.monitoring.matchers.LocationMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsArray
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class LocationRepositoryImplTest {

    @MockkBean
    lateinit var locationDao: LocationDao

    @Autowired
    lateinit var locationRepository: LocationRepository

    @Test
    fun `When findAll, then return all locations`() {
        val locations = listOf(
            Location(id = 1L, name = "Kitchen"),
            Location(id = 2L, name = "Living room")
        )
        every { locationDao.findAll() } returns locations

        val result = locationRepository.findAll()

        assertThat(
            result.toTypedArray(),
            IsArray.array(
                LocationMatchers.isEqual(locationName = "Kitchen"),
                LocationMatchers.isEqual(locationName = "Living room")
            )
        )
    }

    @Test
    fun `Given location exists, when findByNameOrCreate, then return existing location`() {
        val location = Location(id = 1L, name = "Bedroom")
        every { locationDao.findByName(name = "Bedroom") } returns Optional.of(location)

        val result = locationRepository.findByNameOrCreate("Bedroom")

        assertThat(result, LocationMatchers.isEqual(location))

        verify { locationDao.save<Location>(any()) wasNot Called }
    }

    @Test
    fun `Given location doesn't exist, when findByNameOrCreate, then create and return new location`() {
        val location = Location(id = 1L, name = "Bedroom")
        every { locationDao.findByName(name = "Bedroom") } returns Optional.empty()
        every { locationDao.save<Location>(any()) } returnsArgument 0

        val result = locationRepository.findByNameOrCreate("Bedroom")

        assertThat(result, LocationMatchers.isEqual(location))

        verify(exactly = 1) { locationDao.save<Location>(match { it.name == "Bedroom" }) }
    }
}
