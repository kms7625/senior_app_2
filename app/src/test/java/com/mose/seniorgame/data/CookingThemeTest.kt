package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CookingThemeTest {

    @Test
    fun `candidates returns requested count of target-decoy pairs`() {
        val candidates = CookingTheme.candidates(4)
        assertEquals(4, candidates.size)
        assertEquals("두부" to "순두부", candidates.first())
    }

    @Test
    fun `decoyOf returns the matched decoy for a known target`() {
        assertEquals("순두부", CookingTheme.decoyOf("두부"))
    }

    @Test
    fun `decoyOf returns null for an unknown item`() {
        assertNull(CookingTheme.decoyOf("존재하지않는품목"))
    }

    @Test
    fun `totalPrice sums the price of every selected item`() {
        // 두부 1500 + 계란 4000 (docs/theme-item-pool.md 가격 기준, CookingTheme.pairs 참고)
        assertEquals(5500, CookingTheme.totalPrice(listOf("두부", "계란")))
    }

    @Test
    fun `totalPrice ignores unknown items as zero-priced`() {
        assertEquals(0, CookingTheme.totalPrice(listOf("존재하지않는품목")))
    }
}
