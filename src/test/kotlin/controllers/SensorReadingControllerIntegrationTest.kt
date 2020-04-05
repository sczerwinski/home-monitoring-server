package it.czerwinski.home.monitoring.controllers

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.czerwinski.home.monitoring.annotations.IntegrationTest
import it.czerwinski.home.monitoring.matchers.LocationMatchers
import it.czerwinski.home.monitoring.matchers.SensorReadingMatchers
import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorType
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsArray
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@IntegrationTest
class SensorReadingControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val entityManager: TestEntityManager
) {

    @MockkBean
    lateinit var clock: Clock

    @Test
    fun `Given location exists, when GET latest sensor reading, then return latest reading`() {
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
    fun `Given location doesn't exist, when GET latest sensor reading, then return nothing`() {
        mockMvc.perform(
            get("/location/Bedroom/type/temperature/reading")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().bytes(byteArrayOf()))
    }

    @Test
    fun `Given location exists, when GET sensor readings by date, then return latest reading`() {
        val location = Location(name = "Living room")
            .let { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        val readings = listOf(
            SensorReading(
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = LocalDateTime.of(2020, 1, 1, 12, 30),
                value = 23.4
            ),
            SensorReading(
                location = location,
                type = SensorType.TEMPERATURE,
                dateTime = LocalDateTime.of(2020, 1, 1, 13, 30),
                value = 24.5
            )
        )
        for (reading in readings) {
            entityManager.persist(reading)
        }
        entityManager.flush()

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
    fun `Given location doesn't exist, when GET sensor readings by date, then return empty array`() {
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
    fun `Given location exists, when POST sensor reading, then save new sensor reading`() {
        val dateTime = LocalDateTime.of(2020, 1, 1, 12, 30)
        every { clock.instant() } returns dateTime.toInstant(ZoneOffset.UTC)
        every { clock.zone } returns ZoneId.systemDefault()

        val location = Location(name = "Kitchen")
            .let { location -> location.copy(id = entityManager.persistAndGetId(location) as Long) }
        entityManager.flush()

        mockMvc.perform(
            post("/location/Kitchen/type/humidity/reading")
                .contentType(MediaType.APPLICATION_JSON)
                .content("12.3")
        )
            .andExpect(status().isCreated)
            .andExpect(content().bytes(byteArrayOf()))

        val sensorReading = entityManager.find(SensorReading::class.java, 1L)

        assertThat(
            sensorReading,
            SensorReadingMatchers.isEqual(
                locationMatcher = LocationMatchers.isEqual(location),
                type = SensorType.HUMIDITY,
                dateTime = dateTime,
                value = 12.3
            )
        )
    }

    @Test
    fun `Given location doesn't exist, when POST sensor reading, then save location and new sensor reading`() {
        val dateTime = LocalDateTime.of(2020, 1, 1, 12, 30)
        every { clock.instant() } returns dateTime.toInstant(ZoneOffset.UTC)
        every { clock.zone } returns ZoneId.systemDefault()

        mockMvc.perform(
            post("/location/Kitchen/type/humidity/reading")
                .contentType(MediaType.APPLICATION_JSON)
                .content("12.3")
        )
            .andExpect(status().isCreated)
            .andExpect(content().bytes(byteArrayOf()))

        val locations = entityManager.entityManager
            .createQuery("select l from Location l", Location::class.java)
            .resultList
        val sensorReadings = entityManager.entityManager
            .createQuery("select r from SensorReading r", SensorReading::class.java)
            .resultList

        assertThat(
            locations.toTypedArray(),
            IsArray.array(LocationMatchers.isEqual(locationName = "Kitchen"))
        )
        assertThat(
            sensorReadings.toTypedArray(),
            IsArray.array(
                SensorReadingMatchers.isEqual(
                    locationMatcher = LocationMatchers.isEqual(locationName = "Kitchen"),
                    type = SensorType.HUMIDITY,
                    dateTime = dateTime,
                    value = 12.3
                )
            )
        )
    }
}
