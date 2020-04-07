package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.annotations.IntegrationTest
import it.czerwinski.home.monitoring.model.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@IntegrationTest
class SensorTypeControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val entityManager: TestEntityManager
) {

    @Test
    fun `Given location exists, when GET sensor types, then return sensor types with readings`() {
        val location = Location(name = "Bedroom")
            .let { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        val reading = SensorReading(
            location = location,
            type = SensorType.TEMPERATURE,
            dateTime = LocalDateTime.of(2020, 1, 1, 12, 30),
            value = 23.4
        )
        entityManager.persist(reading)
        entityManager.flush()

        mockMvc.perform(
            MockMvcRequestBuilders.get("/location/Bedroom/types")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]]").value("TEMPERATURE"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[1]").doesNotExist())
    }

    @Test
    fun `Given location doesn't exist, when GET sensor types, then return empty array`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/location/Bedroom/types")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.[0]]").doesNotExist())
    }
}
