package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutePlanTest {

    private val sections = listOf("채소·신선 코너", "가공식품 코너", "양념 코너")
    private val checkout = RoutePlan.CHECKOUT

    // 두부(채소·신선)와 고추장(양념)을 골랐을 때 — 가공식품 코너는 필요 없다
    private val itemCorners = mapOf("두부" to "채소·신선 코너", "고추장" to "양념 코너")

    @Test
    fun `shortest visits only the given corners left to right then checkout`() {
        assertEquals(listOf("채소·신선 코너", "양념 코너", checkout), RoutePlan.shortest(listOf("양념 코너", "채소·신선 코너"), sections))
        assertEquals(sections + checkout, RoutePlan.shortest(sections, sections))
        assertEquals(4, RoutePlan.distance(sections + checkout, sections))
    }

    @Test
    fun `skipping unneeded corners in the shortest order gets praise`() {
        val order = listOf("채소·신선 코너", "양념 코너", checkout)
        assertEquals("가장 덜 걷는 길을 찾으셨어요!", RoutePlan.feedback(order, sections, itemCorners))
    }

    @Test
    fun `a missing corner names the item and where it is`() {
        val order = listOf("채소·신선 코너", checkout)
        assertEquals("고추장은 양념 코너에 있어요. 그 코너도 들러 주세요", RoutePlan.feedback(order, sections, itemCorners))
    }

    @Test
    fun `an unneeded corner is pointed out`() {
        val order = listOf("채소·신선 코너", "가공식품 코너", "양념 코너", checkout)
        assertEquals("가공식품 코너는 이번엔 안 들러도 되는 코너예요", RoutePlan.feedback(order, sections, itemCorners))
    }

    @Test
    fun `longer order of the needed corners suggests the shortest one`() {
        val order = listOf("양념 코너", "채소·신선 코너", checkout)
        assertTrue(RoutePlan.feedback(order, sections, itemCorners).startsWith("좋아요! 채소·신선 코너 → 양념 코너 → 계산대"))
    }

    @Test
    fun `checkout not last gets a checkout hint`() {
        val order = listOf(checkout, "채소·신선 코너", "양념 코너")
        assertTrue(RoutePlan.feedback(order, sections, itemCorners).startsWith("계산대는 마지막에"))
    }
}
