package it.czerwinski.home.monitoring.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.czerwinski.home.monitoring.model.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [SensorTypeController::class])
class SensorTypeControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var locationRepository: LocationRepository

    @MockkBean
    lateinit var sensorTypeRepository: SensorTypeRepository

    @Test
    fun `Given location exists, when GET sensor types, then return sensor types with readings`() {
        val location = Location(id = 1L, name = "Bedroom")
        every { locationRepository.findByName(name = "Bedroom") } returns location
        every { sensorTypeRepository.findByLocation(location = location) } returns listOf(SensorType.TEMPERATURE)

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
        every { locationRepository.findByName(name = "Bedroom") } returns null

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
