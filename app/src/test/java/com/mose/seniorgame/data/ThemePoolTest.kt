package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemePoolTest {

    @Test
    fun `all 6 themes from theme-item-pool md are present with 10 items each`() {
        assertEquals(6, ThemePool.all.size)
        ThemePool.all.forEach { theme ->
            assertEquals("${theme.label} 품목 수", 10, theme.items.size)
        }
    }

    @Test
    fun `every item sits in one of its theme's corners and no corner is empty`() {
        ThemePool.all.forEach { theme ->
            theme.items.forEach { assertTrue("${theme.label} ${it.name}", it.section in theme.sections.indices) }
            theme.sections.indices.forEach { i ->
                assertTrue("${theme.label} ${theme.sections[i]} 비어 있음", theme.items.any { it.section == i })
            }
        }
    }

    @Test
    fun `neededSections lists only corners of the picked items in map order`() {
        assertEquals(listOf("채소·신선 코너", "양념 코너"), ThemePool.cooking.neededSections(listOf("고추장", "두부", "우유")))
    }

    @Test
    fun `every theme has exactly 3 route sections for RouteScreen`() {
        ThemePool.all.forEach { theme ->
            assertEquals("${theme.label} 코너 수", 3, theme.sections.size)
        }
    }

    @Test
    fun `theme ids are unique`() {
        val ids = ThemePool.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `candidates returns requested count of target-decoy pairs`() {
        val candidates = ThemePool.cooking.candidates(4)
        assertEquals(4, candidates.size)
        assertEquals("두부" to "순두부", candidates.first())
    }

    @Test
    fun `decoyOf returns the matched decoy for a known target`() {
        assertEquals("순두부", ThemePool.cooking.decoyOf("두부"))
    }

    @Test
    fun `decoyOf returns null for an unknown item`() {
        assertNull(ThemePool.cooking.decoyOf("존재하지않는품목"))
    }

    @Test
    fun `totalPrice sums the price of every selected item`() {
        assertEquals(5500, ThemePool.cooking.totalPrice(listOf("두부", "계란")))
    }

    @Test
    fun `totalPrice ignores unknown items as zero-priced`() {
        assertEquals(0, ThemePool.cooking.totalPrice(listOf("존재하지않는품목")))
    }

    @Test
    fun `byId falls back to cooking for an unknown id`() {
        assertTrue(ThemePool.byId("존재하지않는테마") === ThemePool.cooking)
    }

    @Test
    fun `byId finds each registered theme by its own id`() {
        ThemePool.all.forEach { theme ->
            assertTrue(ThemePool.byId(theme.id) === theme)
        }
    }
}
