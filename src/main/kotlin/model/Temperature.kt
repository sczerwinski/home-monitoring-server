package it.czerwinski.home.monitoring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Temperature(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore val id: Long? = null,
    val time: LocalDateTime,
    val temperature: Double
)
