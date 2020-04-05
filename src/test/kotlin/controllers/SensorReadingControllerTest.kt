package it.czerwinski.home.monitoring.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import it.czerwinski.home.monitoring.model.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.*

@WebMvcTest(controllers = [SensorReadingController::class])
class SensorReadingControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @MockkBean
    lateinit var locationRepository: LocationRepository

    @MockkBean
    lateinit var sensorReadingRepository: SensorReadingRepository

    @MockkBean
    lateinit var clock: Clock

    @Test
    fun `Given location exists, when GET latest sensor reading, then return latest reading`() {
        val location = Location(id = 1L, name = "Bedroom")
        val reading = SensorReading(
            id = 123L,
            location = location,
            type = SensorType.TEMPERATURE,
            dateTime = LocalDateTime.of(2020, 1, 1, 12, 30),
            value = 23.4
        )
        every { locationRepository.findByName(name = "Bedroom") } returns location
        every {
            sensorReadingRepository.findLatestReading(location = location, type = SensorType.TEMPERATURE)
        } returns reading

        mockMvc.perform(
            get("/location/Bedroom/type/temperature/reading")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").doesNotExist())
            .andExpect(jsonPath("\$.location").doesNotExist())
            .andExpect(jsonPath("\$.type").doesNotExist())
            .andExpect(jsonPath("\$.dateTime").value("2020-01-01T12:30:00"))
            .andExpect(jsonPath("\$.value").value(23.4))
    }

    @Test
    fun `Given location doesn't exists, when GET latest sensor reading, then return nothing`() {
        every { locationRepository.findByName(name = "Bedroom") } returns null

        mockMvc.perform(
            get("/location/Bedroom/type/temperature/reading")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().bytes(byteArrayOf()))
    }

    @Test
    fun `Given location exists, when GET sensor readings by date, then return latest reading`() {
        val date = LocalDate.of(2020, 1, 1)
        val location = Location(id = 1L, name = "Living room")
        val readings = listOf(
            SensorReading(
                id = 123L,
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = LocalDateTime.of(2020, 1, 1, 12, 30),
                value = 23.4
            ),
            SensorReading(
                id = 124L,
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = LocalDateTime.of(2020, 1, 1, 13, 30),
                value = 24.5
            )
        )
        every { locationRepository.findByName(name = "Living room") } returns location
        every {
            sensorReadingRepository.findByDate(location = location, type = SensorType.TEMPERATURE, date = date)
        } returns readings

        mockMvc.perform(
            get("/location/Living room/type/temperature/readings/2020-01-01")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").doesNotExist())
            .andExpect(jsonPath("\$.[0].location").doesNotExist())
            .andExpect(jsonPath("\$.[0].type").doesNotExist())
            .andExpect(jsonPath("\$.[0].dateTime").value("2020-01-01T12:30:00"))
            .andExpect(jsonPath("\$.[0].value").value(23.4))
            .andExpect(jsonPath("\$.[1].id").doesNotExist())
            .andExpect(jsonPath("\$.[1].location").doesNotExist())
            .andExpect(jsonPath("\$.[1].type").doesNotExist())
            .andExpect(jsonPath("\$.[1].dateTime").value("2020-01-01T13:30:00"))
            .andExpect(jsonPath("\$.[1].value").value(24.5))
    }

    @Test
    fun `Given location doesn't exists, when GET sensor readings by date, then return empty array`() {
        every { locationRepository.findByName(name = "Living room") } returns null

        mockMvc.perform(
            get("/location/Living room/type/temperature/readings/2020-01-01")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$").isArray)
            .andExpect(jsonPath("\$.[0]").doesNotExist())
    }

    @Test
    fun `When POST sensor reading, then save new sensor reading`() {
        val dateTime = LocalDateTime.of(2020, 1, 1, 12, 30)
        val location = Location(id = 1L, name = "Kitchen")
        every { clock.instant() } returns dateTime.toInstant(ZoneOffset.UTC)
        every { clock.zone } returns ZoneId.systemDefault()
        every { locationRepository.findByNameOrCreate(name = "Kitchen") } returns location
        every { sensorReadingRepository.save(sensorReading = any()) } returns Unit

        mockMvc.perform(
            post("/location/Kitchen/type/humidity/reading")
                .contentType(MediaType.APPLICATION_JSON)
                .content("12.3")
        )
            .andExpect(status().isCreated)
            .andExpect(content().bytes(byteArrayOf()))

        verify(exactly = 1) {
            sensorReadingRepository.save(
                SensorReading(
                    location = location,
                    type = SensorType.HUMIDITY,
                    dateTime = dateTime,
                    value = 12.3
                )
            )
        }
    }
}
