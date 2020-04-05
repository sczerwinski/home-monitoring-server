package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.Temperature
import it.czerwinski.home.monitoring.db.TemperatureRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDateTime

@RestController
class TemperatureController(
    private val repository: TemperatureRepository,
    private val clock: Clock
) {

    @GetMapping(
        name = "Get latest temperature",
        path = ["/temperature"]
    )
    fun getLatestTemperature(): Temperature? =
        repository.findFirst1ByOrderByTimeDesc().orElse(null)

    @GetMapping(
        name = "Get temperature for time period",
        path = ["/temperature/start/{start}/end/{end}"]
    )
    fun getTemperatureBetween(
        @PathVariable(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime,
        @PathVariable(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime
    ): List<Temperature> =
        repository.findByTimeBetween(start, end).toList()

    @PostMapping(
        name = "Post current temperature",
        path = ["/temperature"]
    )
    fun putTemperature(
        @RequestBody value: Double
    ): ResponseEntity<Temperature> =
        repository.save(
            Temperature(
                time = LocalDateTime.now(clock),
                temperature = value
            )
        ).let { ResponseEntity(it, HttpStatus.CREATED) }
}
