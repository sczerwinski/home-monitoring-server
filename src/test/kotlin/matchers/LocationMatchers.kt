package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Location
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * A set of Matchers for [Location] assertions.
 */
object LocationMatchers {

    /**
     * Creates a new matcher evaluating whether two locations are equal.
     */
    fun isEqual(
        location: Location
    ): Matcher<Location?> =
        isEqual(
            locationName = location.name
        )

    /**
     * Creates a new matcher evaluating whether two locations are equal.
     */
    fun isEqual(
        locationName: String
    ): Matcher<Location?> =
        IsEqual(
            expectedName = locationName
        )

    private class IsEqual(
        private val expectedName: String
    ) : BaseMatcher<Location?>() {

        override fun describeTo(description: Description?) {
            description
                ?.appendText("equalTo(name=")
                ?.appendValue(expectedName)
                ?.appendText(")")
        }

        override fun matches(actual: Any?): Boolean = with (actual as Location) {
            name == expectedName
        }
    }
}
