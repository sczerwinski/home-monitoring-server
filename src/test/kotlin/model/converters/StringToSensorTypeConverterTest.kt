package it.czerwinski.home.monitoring.model.converters

import it.czerwinski.home.monitoring.model.SensorType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EmptySource
import org.junit.jupiter.params.provider.ValueSource

class StringToSensorTypeConverterTest {

    @ParameterizedTest(name = "When \"{0}\", then return temperature sensor type")
    @ValueSource(strings = ["temperature", "Temperature", "TEMPERATURE"])
    fun `When string representation of temperature, then return temperature sensor type`(
        string: String
    ) {
        val converter = StringToSensorTypeConverter()

        val result = converter.convert(string)

        assertEquals(SensorType.TEMPERATURE, result)
    }

    @ParameterizedTest(name = "When \"{0}\", then return humidity sensor type")
    @ValueSource(strings = ["humidity", "Humidity", "HUMIDITY"])
    fun `When string representation of humidity, then return humidity sensor type`(
        string: String
    ) {
        val converter = StringToSensorTypeConverter()

        val result = converter.convert(string)

        assertEquals(SensorType.HUMIDITY, result)
    }

    @ParameterizedTest(name = "When \"{0}\", then return null")
    @EmptySource
    @ValueSource(strings = ["unknown"])
    fun `When unknown sensor type, then return null`(
        string: String
    ) {
        val converter = StringToSensorTypeConverter()

        val result = converter.convert(string)

        assertNull(result)
    }
}
