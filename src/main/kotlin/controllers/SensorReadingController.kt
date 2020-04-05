package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.LocationRepository
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorReadingRepository
import it.czerwinski.home.monitoring.model.SensorType
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class SensorReadingController(
    private val locationRepository: LocationRepository,
    private val sensorReadingRepository: SensorReadingRepository,
    private val clock: Clock
) {

    @GetMapping(
        name = "Get latest sensor reading",
        path = ["/location/{locationName}/type/{sensorType}/reading"]
    )
    fun getLatestSensorReading(
        @PathVariable(name = "locationName") locationName: String,
        @PathVariable(name = "sensorType") sensorType: SensorType
    ): SensorReading? {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorReadingRepository.findLatestReading(location, sensorType) else null
    }

    @GetMapping(
        name = "Get sensor readings by date",
        path = ["/location/{locationName}/type/{sensorType}/readings/{date}"]
    )
    fun getSensorReadingsByDate(
        @PathVariable(name = "locationName") locationName: String,
        @PathVariable(name = "sensorType") sensorType: SensorType,
        @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): List<SensorReading> {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorReadingRepository.findByDate(location, sensorType, date) else emptyList()
    }

    @PostMapping(
        name = "Post sensor reading",
        path = ["/location/{locationName}/type/{sensorType}/reading"]
    )
    fun postSensorReading(
        @PathVariable(name = "locationName") locationName: String,
        @PathVariable(name = "sensorType") sensorType: SensorType,
        @RequestBody readingValue: Double
    ): ResponseEntity<out Any> {
        val location = locationRepository.findByNameOrCreate(locationName)
        sensorReadingRepository.save(
            SensorReading(
                location = location,
                type = sensorType,
                dateTime = LocalDateTime.now(clock),
                value = readingValue
            )
        )
        return ResponseEntity<Any>(HttpStatus.CREATED)
    }
}
