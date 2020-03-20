package it.czerwinski.home.monitoring.controllers

import it.czerwinski.home.monitoring.model.Temperature
import it.czerwinski.home.monitoring.db.TemperatureRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDateTime

@RestController
class TemperatureController(
    private val repository: TemperatureRepository,
    private val clock: Clock
) {

    @GetMapping(path = ["/temperature"])
    fun getCurrentTemperature() = repository.findFirst1ByOrderByTimeDesc()

    @GetMapping(path = ["/temperature/start/{start}/end/{end}"])
    fun getCurrentTemperature(
        @PathVariable(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: LocalDateTime,
        @PathVariable(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: LocalDateTime
    ) = repository.findByTimeBetween(start, end)

    @PostMapping(path = ["/temperature"])
    fun putTemperature(
        @RequestBody value: Double
    ) = repository.save(
        Temperature(
            time = LocalDateTime.now(clock),
            temperature = value
        )
    )
}
