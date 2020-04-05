package it.czerwinski.home.monitoring.matchers

import it.czerwinski.home.monitoring.model.Location
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot
import org.junit.jupiter.api.Test

class LocationMatchersIsEqualTest {

    @Test
    fun `When the same location, then matches`() {
        val location = Location(
            id = 123L,
            name = "Kitchen"
        )

        assertThat(location, LocationMatchers.isEqual(location))
    }

    @Test
    fun `When different id, then matches`() {
        val location1 = Location(
            id = 123L,
            name = "Kitchen"
        )
        val location2 = Location(
            id = 456L,
            name = "Kitchen"
        )

        assertThat(location1, LocationMatchers.isEqual(location2))
    }

    @Test
    fun `When different name, then doesn't match`() {
        val location1 = Location(
            id = 123L,
            name = "Kitchen"
        )
        val location2 = Location(
            id = 123L,
            name = "Living room"
        )

        assertThat(location1, IsNot(LocationMatchers.isEqual(location2)))
    }
}
