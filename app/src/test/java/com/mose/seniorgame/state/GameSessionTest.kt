package com.mose.seniorgame.state

import com.mose.seniorgame.data.ThemePool
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSessionTest {

    @After
    fun tearDown() {
        // 싱글턴 상태가 테스트 간에 새어나가지 않도록 매번 초기화한다.
        GameSession.resetForTest()
    }

    @Test
    fun `toggleSelected adds then removes the same item`() {
        GameSession.toggleSelected("두부")
        assertTrue(GameSession.selectedItems.contains("두부"))

        GameSession.toggleSelected("두부")
        assertFalse(GameSession.selectedItems.contains("두부"))
    }

    @Test
    fun `showListHint is true for round 1 and 2, false from round 3`() {
        assertTrue(GameSession.showListHint) // round 1

        GameSession.advanceRound() // round 2
        assertTrue(GameSession.showListHint)

        GameSession.advanceRound() // round 3
        assertFalse(GameSession.showListHint)
    }

    @Test
    fun `advanceRound increments the round and clears this round's picks`() {
        GameSession.toggleSelected("두부")
        GameSession.toggleCollected("두부")

        GameSession.advanceRound()

        assertEquals(2, GameSession.roundNumber.value)
        assertTrue(GameSession.selectedItems.isEmpty())
        assertTrue(GameSession.collectedItems.isEmpty())
    }

    @Test
    fun `selectTheme switches the current theme and clears this round's picks`() {
        GameSession.toggleSelected("두부")

        GameSession.selectTheme(ThemePool.tools)

        assertEquals(ThemePool.tools, GameSession.currentTheme.value)
        assertTrue(GameSession.selectedItems.isEmpty())
    }
}
