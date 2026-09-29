package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class CheckoutQuizTest {

    @Test
    fun `three distinct choices including the correct total`() {
        repeat(50) { seed ->
            val choices = CheckoutQuiz.choices(5500, Random(seed))
            assertEquals(3, choices.size)
            assertEquals(3, choices.toSet().size)
            assertTrue(choices.contains(5500))
        }
    }

    @Test
    fun `small totals never produce zero or negative choices`() {
        repeat(50) { seed ->
            val choices = CheckoutQuiz.choices(800, Random(seed))
            assertEquals(3, choices.size)
            assertTrue(choices.all { it > 0 })
            assertTrue(choices.contains(800))
        }
    }
}
