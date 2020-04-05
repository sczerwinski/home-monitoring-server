package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.LocationRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationController(
    private val locationRepository: LocationRepository
) {

    @GetMapping(
        name = "Get all locations",
        path = ["/locations"]
    )
    fun getAllLocations(): List<Location> =
        locationRepository.findAll()
}
