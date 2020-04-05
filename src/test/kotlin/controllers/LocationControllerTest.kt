package it.czerwinski.home.monitoring.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.LocationRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [LocationController::class])
class LocationControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var locationRepository: LocationRepository

    @Test
    fun `When GET all locations, then return list of available locations`() {
        val locations = listOf(
            Location(id = 1L, name = "Kitchen"),
            Location(id = 2L, name = "Living room")
        )
        every { locationRepository.findAll() } returns locations

        mockMvc.perform(get("/locations").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(1L))
            .andExpect(jsonPath("\$.[0].name").value("Kitchen"))
            .andExpect(jsonPath("\$.[1].id").value(2L))
            .andExpect(jsonPath("\$.[1].name").value("Living room"))
    }
}
