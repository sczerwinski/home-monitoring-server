package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Location
import it.czerwinski.home.monitoring.model.SensorReading
import it.czerwinski.home.monitoring.model.SensorType
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.time.LocalDateTime

/**
 * A set of Matchers for [SensorReading] assertions.
 */
object SensorReadingMatchers {

    /**
     * Creates a new matcher evaluating whether two sensor readings are equal.
     */
    fun isEqual(
        sensorReading: SensorReading
    ): Matcher<SensorReading?> =
        isEqual(
            locationMatcher = LocationMatchers.isEqual(sensorReading.location),
            type = sensorReading.type,
            dateTime = sensorReading.dateTime,
            value = sensorReading.value
        )

    /**
     * Creates a new matcher evaluating whether two sensor readings are equal.
     */
    fun isEqual(
        locationMatcher: Matcher<Location?>,
        type: SensorType,
        dateTime: LocalDateTime,
        value: Double
    ): Matcher<SensorReading?> =
        IsEqual(
            locationMatcher = locationMatcher,
            expectedType = type,
            expectedDateTime = dateTime,
            expectedValue = value
        )

    private class IsEqual(
        private val locationMatcher: Matcher<Location?>,
        private val expectedType: SensorType,
        private val expectedDateTime: LocalDateTime,
        private val expectedValue: Double
    ) : BaseMatcher<SensorReading?>() {

        override fun describeTo(description: Description?) {
            description
                ?.appendText("equalTo(location=")
                ?.appendDescriptionOf(locationMatcher)
                ?.appendText(", type=")
                ?.appendValue(expectedType)
                ?.appendText(", dateTime=")
                ?.appendValue(expectedDateTime)
                ?.appendText(", value=")
                ?.appendValue(expectedValue)
                ?.appendText(")")
        }

        override fun matches(actual: Any?): Boolean = with (actual as SensorReading) {
            locationMatcher.matches(location)
                    && type == expectedType
                    && dateTime == expectedDateTime
                    && value == expectedValue
        }
    }
}
