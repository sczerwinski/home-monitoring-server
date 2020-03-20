package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Temperature
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.time.LocalDateTime

object TemperatureMatchers {

    fun isEqual(
        temperature: Temperature
    ): Matcher<Temperature> =
        isEqual(
            time = temperature.time,
            temperature = temperature.temperature
        )

    fun isEqual(
        time: LocalDateTime,
        temperature: Double
    ): Matcher<Temperature> =
        IsEqual(
            expectedTime = time,
            expedtedTemperature = temperature
        )

    private class IsEqual(
        private val expectedTime: LocalDateTime,
        private val expedtedTemperature: Double
    ) : BaseMatcher<Temperature>() {

        override fun describeTo(description: Description?) {
            description
                ?.appendText("equalTo(time=")
                ?.appendValue(expectedTime)
                ?.appendText(", temperature=")
                ?.appendValue(expedtedTemperature)
                ?.appendText(")")
        }

        override fun matches(actual: Any?): Boolean = with (actual as Temperature) {
            time == expectedTime && temperature == expedtedTemperature
        }
    }
}
