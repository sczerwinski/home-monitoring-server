package it.czerwinski.home.monitoring.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.LocationRepository
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Api(description = "Provides information about locations")
@RestController
class LocationController(
    private val locationRepository: LocationRepository
) {

    @ApiOperation(value = "Returns all locations with recorded sensor readings")
    @GetMapping(
        path = ["/locations"],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getAllLocations(): List<Location> =
        locationRepository.findAll()
}
