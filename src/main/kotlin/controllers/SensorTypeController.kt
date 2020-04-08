package it.czerwinski.home.monitoring.controllers

import io.swagger.annotations.*
import it.czerwinski.home.monitoring.model.LocationRepository
import it.czerwinski.home.monitoring.model.SensorType
import it.czerwinski.home.monitoring.model.SensorTypeRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Api(description = "Provides information about sensor types")
@RestController
class SensorTypeController(
    private val locationRepository: LocationRepository,
    private val sensorTypeRepository: SensorTypeRepository
) {

    @ApiOperation(value = "Returns all sensor types with recorded sensor readings at given location")
    @GetMapping(
        path = ["/location/{locationName}/types"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getSensorTypes(
        @ApiParam(value = "Location name", required = true)
        @PathVariable(name = "locationName")
        locationName: String
    ): List<SensorType> {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorTypeRepository.findByLocation(location) else emptyList()
    }
}
