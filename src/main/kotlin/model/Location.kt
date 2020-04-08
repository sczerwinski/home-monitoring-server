package it.czerwinski.home.monitoring.model

import io.swagger.annotations.ApiModelProperty
import javax.persistence.*

/**
 * A location associated with sensor readings.
 */
@Entity
@Table(
    name = "locations",
    indexes = [
        Index(
            name = "idx_location_name",
            columnList = "name",
            unique = true
        )
    ]
)
data class Location (

    /**
     * Unique identifier of the location.
     */
    @ApiModelProperty(notes = "Location ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
        name = "id",
        nullable = false
    )
    val id: Long? = null,

    /**
     * Location name.
     */
    @ApiModelProperty(notes = "Location name")
    @Column(
        name = "name",
        nullable = false
    )
    val name: String
)
