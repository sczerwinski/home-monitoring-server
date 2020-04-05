package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.annotations.IntegrationTest
import it.czerwinski.home.monitoring.model.Location
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@IntegrationTest
class LocationControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val entityManager: TestEntityManager
) {

    @Test
    fun `When GET all locations, then return list of available locations`() {
        listOf("Kitchen", "Living room")
            .map { locationName -> Location(name = locationName) }
            .forEach { location -> entityManager.persist(location) }
        entityManager.flush()

        mockMvc.perform(get("/locations").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").exists())
            .andExpect(jsonPath("\$.[0].name").value("Kitchen"))
            .andExpect(jsonPath("\$.[1].id").exists())
            .andExpect(jsonPath("\$.[1].name").value("Living room"))

    }
}
