package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.LocationRepository
import it.czerwinski.home.monitoring.model.SensorType
import it.czerwinski.home.monitoring.model.SensorTypeRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SensorTypeController(
    private val locationRepository: LocationRepository,
    private val sensorTypeRepository: SensorTypeRepository
) {

    @GetMapping(
        name = "Get sensor types",
        path = ["/location/{locationName}/types"]
    )
    fun getSensorTypes(
        @PathVariable(name = "locationName") locationName: String
    ): List<SensorType> {
        val location = locationRepository.findByName(locationName)
        return if (location != null) sensorTypeRepository.findByLocation(location) else emptyList()
    }
}
