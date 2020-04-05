package it.czerwinski.home.monitoring.model

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.czerwinski.home.monitoring.db.LocationDao
import it.czerwinski.home.monitoring.matchers.LocationMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsArray
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
}
