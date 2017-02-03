package com.jonnymatts.classy

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class RandomizerTest {

    @JvmField @Rule val expectedException: ExpectedException = ExpectedException.none()

    val randomizer: Randomizer = Randomizer()

    @Test
    fun randomEntryOfEmptyListThrowsException() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("empty")

        randomizer.randomEntry(listOf<String>())
    }

    @Test
    fun randomEntryOfListWithOneElementReturnsElement() {
        val got: String = randomizer.randomEntry(listOf("blah"))

        assertThat(got).isEqualTo("blah")
    }
}