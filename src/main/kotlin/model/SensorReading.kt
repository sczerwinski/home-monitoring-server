package it.czerwinski.home.monitoring.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

/**
 * A single sensor reading.
 */
@Entity
@Table(
    name = "sensor_readings",
    indexes = [
        Index(
            name = "idx_sensor_reading_location_type_date_time",
            columnList = "location_id, sensor_type, date_time"
        )
    ]
)
@NamedQuery(
    name = "SensorReading.findSensorTypesByLocation",
    query = "select distinct(r.type) from SensorReading r where r.location = :location"
)
data class SensorReading(

    /**
     * Unique identifier of the sensor reading.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
        name = "id",
        nullable = false
    )
    @JsonIgnore
    val id: Long? = null,

    /**
     * Location associated with the sensor reading.
     */
    @ManyToOne(
        targetEntity = Location::class,
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.EAGER,
        optional = false
    )
    @JoinColumn(
        name = "location_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_sensor_readings_location"))
    @JsonIgnore
    val location: Location,

    /**
     * Sensor type associated with the reading.
     */
    @Enumerated(EnumType.STRING)
    @Column(
        name = "sensor_type",
        nullable = false
    )
    @JsonIgnore
    val type: SensorType,

    /**
     * Date and time of the sensor reading.
     */
    @Column(
        name = "date_time",
        nullable = false
    )
    val dateTime: LocalDateTime,

    /**
     * Sensor reading value.
     */
    @Column(
        name = "value",
        nullable = false
    )
    val value: Double
)
