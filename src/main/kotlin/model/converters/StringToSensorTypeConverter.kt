package it.czerwinski.home.monitoring.model.converters

import it.czerwinski.home.monitoring.model.SensorType
import org.springframework.core.convert.converter.Converter

/**
 * Case insensitive [String] to [SensorType] converter.
 */
class StringToSensorTypeConverter : Converter<String, SensorType> {

    /**
     * Converts [String] to [SensorType] ignoring character case.
     */
    override fun convert(source: String): SensorType? =
        SensorType.values().firstOrNull { it.name.equals(source, ignoreCase = true) }
}
