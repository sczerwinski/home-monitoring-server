package it.czerwinski.home.monitoring.controllers

import io.swagger.annotations.*
import it.czerwinski.home.monitoring.model.LocationRepository
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorReadingRepository
import it.czerwinski.home.monitoring.model.SensorType
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Api(description = "Stores and provides sensor readings")
@RestController
class SensorReadingController(
    private val locationRepository: LocationRepository,
    private val sensorReadingRepository: SensorReadingRepository,
    private val clock: Clock
) {

    @ApiOperation(value = "Returns latest sensor reading of given type at given location")
    @ApiResponses(ApiResponse(code = 400, message = "Invalid sensor type"))
    @GetMapping(
        path = ["/location/{locationName}/type/{sensorType}/reading"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getLatestSensorReading(
        @ApiParam(value = "Location name", required = true)
        @PathVariable(name = "locationName")
        locationName: String,
        @ApiParam(value = "Sensor type", required = true)
        @PathVariable(name = "sensorType")
        sensorType: SensorType
    ): SensorReading? {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorReadingRepository.findLatestReading(location, sensorType) else null
    }

    @ApiOperation(value = "Returns all sensor readings of given type recorded on a specific day at given location")
    @ApiResponses(ApiResponse(code = 400, message = "Invalid sensor type"))
    @GetMapping(
        path = ["/location/{locationName}/type/{sensorType}/readings/{date}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getSensorReadingsByDate(
        @ApiParam(value = "Location name", required = true)
        @PathVariable(name = "locationName")
        locationName: String,
        @ApiParam(value = "Sensor type", required = true)
        @PathVariable(name = "sensorType")
        sensorType: SensorType,
        @ApiParam(value = "Date (ISO format)", required = true)
        @PathVariable(name = "date")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): List<SensorReading> {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorReadingRepository.findByDate(location, sensorType, date) else emptyList()
    }

    @ApiOperation(value = "Stores a new sensor reading of given type at given location")
    @ApiResponses(ApiResponse(code = 400, message = "Invalid sensor type"))
    @PostMapping(
        path = ["/location/{locationName}/type/{sensorType}/reading"],
        produces = [MediaType.ALL_VALUE]
    )
    fun postSensorReading(
        @ApiParam(value = "Location name", required = true)
        @PathVariable(name = "locationName")
        locationName: String,
        @ApiParam(value = "Sensor type", required = true)
        @PathVariable(name = "sensorType")
        sensorType: SensorType,
        @ApiParam(value = "Reading value", required = true, example = "24.5")
        @RequestBody
        readingValue: Double
    ): ResponseEntity<out Void> {
        val location = locationRepository.findByNameOrCreate(locationName)
        sensorReadingRepository.save(
            SensorReading(
                location = location,
                type = sensorType,
                dateTime = LocalDateTime.now(clock),
                value = readingValue
            )
        )
        return ResponseEntity<Void>(HttpStatus.CREATED)
    }
}
