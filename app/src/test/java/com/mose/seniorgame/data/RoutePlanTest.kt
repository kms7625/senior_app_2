package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutePlanTest {

    private val sections = listOf("채소 코너", "정육 코너", "유제품 코너")

    @Test
    fun `shortest route walks the top row left to right then checkout`() {
        assertEquals(sections + RoutePlan.CHECKOUT, RoutePlan.shortest(sections))
        assertEquals(4, RoutePlan.distance(RoutePlan.shortest(sections), sections))
    }

    @Test
    fun `shortest order gets praise`() {
        assertEquals("가장 덜 걷는 길을 찾으셨어요!", RoutePlan.feedback(sections + RoutePlan.CHECKOUT, sections))
    }

    @Test
    fun `longer order suggests the shortest one without blocking`() {
        val reversed = sections.reversed() + RoutePlan.CHECKOUT
        assertTrue(RoutePlan.distance(reversed, sections) > 4)
        assertTrue(RoutePlan.feedback(reversed, sections).startsWith("좋아요! 채소 코너 → 정육 코너"))
    }

    @Test
    fun `checkout not last gets a checkout hint`() {
        val order = listOf(RoutePlan.CHECKOUT) + sections
        assertTrue(RoutePlan.feedback(order, sections).startsWith("계산대는 마지막에"))
    }
}
