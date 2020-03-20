package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.matchers.TemperatureMatchers
import it.czerwinski.home.monitoring.model.Temperature
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDateTime

@DataJpaTest
class TemperatureRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val temperatureRepository: TemperatureRepository
) {

    @Test
    fun `When findFirst1ByOrderByTimeDesc then return latest`() {
        val temperatures = listOf(23.0, 23.5, 24.0, 24.5, 24.5, 24.0, 23.5, 23.5)
        for ((minute, temperature) in temperatures.withIndex()) {
            entityManager.persist(
                Temperature(
                    time = LocalDateTime.of(2020, 1, 1, 12, minute),
                    temperature = temperature
                )
            )
        }
        entityManager.flush()

        val result = temperatureRepository.findFirst1ByOrderByTimeDesc()

        assertTrue(result.isPresent)
        assertThat(
            result.get(),
            TemperatureMatchers.isEqual(
                time = LocalDateTime.of(2020, 1, 1, 12, temperatures.lastIndex),
                temperature = temperatures.last()
            )
        )
    }

    @Test
    fun `When findByTimeBetween then return temperatures in time period`() {
        val temperatures = listOf(23.0, 23.5, 24.0, 24.5, 24.5, 24.0, 23.5, 23.5)
        for ((hour, temperature) in temperatures.withIndex()) {
            entityManager.persist(
                Temperature(
                    time = LocalDateTime.of(2020, 1, 1, hour, 30),
                    temperature = temperature
                )
            )
        }
        entityManager.flush()

        val result = temperatureRepository.findByTimeBetween(
            start = LocalDateTime.of(2020, 1, 1, 1, 0),
            end = LocalDateTime.of(2020, 1, 1, 3, 0)
        ).toList()

        assertEquals(2, result.size)
        assertThat(
            result.first(),
            TemperatureMatchers.isEqual(
                time = LocalDateTime.of(2020, 1, 1, 1, 30),
                temperature = temperatures[1]
            )
        )
        assertThat(
            result.last(),
            TemperatureMatchers.isEqual(
                time = LocalDateTime.of(2020, 1, 1, 2, 30),
                temperature = temperatures[2]
            )
        )
    }
}
