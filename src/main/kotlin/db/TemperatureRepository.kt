package it.czerwinski.home.monitoring.db

import it.czerwinski.home.monitoring.model.Temperature
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

interface TemperatureRepository : CrudRepository<Temperature, Long> {
    fun findFirst1ByOrderByTimeDesc(): Optional<Temperature>
    fun findByTimeBetween(start: LocalDateTime, end: LocalDateTime): Iterable<Temperature>
}
