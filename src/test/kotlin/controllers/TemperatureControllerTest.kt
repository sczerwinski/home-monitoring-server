package it.czerwinski.home.monitoring.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import it.czerwinski.home.monitoring.db.TemperatureRepository
import it.czerwinski.home.monitoring.model.Temperature
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@WebMvcTest(controllers = [TemperatureController::class])
class TemperatureControllerTest @Autowired constructor(val mockMvc: MockMvc) {

    @MockkBean
    lateinit var temperatureRepository: TemperatureRepository

    @MockkBean
    lateinit var clock: Clock

    @Test
    fun `When GET temperature then return latest temperature`() {
        val temperature = Temperature(
            id = 123L,
            time = LocalDateTime.of(2020, 1, 1, 12, 30),
            temperature = 12.3
        )
        every { temperatureRepository.findFirst1ByOrderByTimeDesc() } returns Optional.of(temperature)

        mockMvc.perform(
            get("/temperature").accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").doesNotExist())
            .andExpect(jsonPath("\$.time").value("2020-01-01T12:30:00"))
            .andExpect(jsonPath("\$.temperature").value(12.3))
    }

    @Test
    fun `When GET temperature-start-end then return temperatures between`() {
        val temperatures = listOf(
            Temperature(
                id = 123L,
                time = LocalDateTime.of(2020, 1, 1, 12, 30),
                temperature = 12.3
            ),
            Temperature(
                id = 124L,
                time = LocalDateTime.of(2020, 1, 1, 12, 31),
                temperature = 12.4
            )
        )
        every {
            temperatureRepository.findByTimeBetween(
                start = LocalDateTime.of(2020, 1, 1, 12, 0),
                end = LocalDateTime.of(2020, 1, 1, 13, 0)
            )
        } returns temperatures

        mockMvc.perform(
            get("/temperature/start/2020-01-01T12:00:00/end/2020-01-01T13:00:00")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").doesNotExist())
            .andExpect(jsonPath("\$.[0].time").value("2020-01-01T12:30:00"))
            .andExpect(jsonPath("\$.[0].temperature").value(12.3))
            .andExpect(jsonPath("\$.[1].id").doesNotExist())
            .andExpect(jsonPath("\$.[1].time").value("2020-01-01T12:31:00"))
            .andExpect(jsonPath("\$.[1].temperature").value(12.4))
    }

    @Test
    fun `When POST temperature then return latest temperature`() {
        val time = LocalDateTime.of(2020, 1, 1, 12, 30)
        every { clock.instant() } returns time.toInstant(ZoneOffset.UTC)
        every { clock.zone } returns ZoneId.systemDefault()
        every { temperatureRepository.save(any<Temperature>()) } returnsArgument 0

        mockMvc.perform(
            post("/temperature")
                .contentType(MediaType.APPLICATION_JSON)
                .content("12.3")
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        verify {
            temperatureRepository.save(
                withArg<Temperature> { Temperature(time = time, temperature = 12.3) }
            )
        }
    }
}
