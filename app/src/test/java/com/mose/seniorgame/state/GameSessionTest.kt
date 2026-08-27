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
    fun `showListHint defaults to true before any round performance is recorded`() {
        // 온디바이스 AI(DifficultyModel)는 실제 기기(Context)가 있어야 초기화되므로
        // 순수 JVM 단위 테스트 환경에서는 초기화되지 않는다 — 이 경우
        // DifficultyModel.predictHideHint가 안전한 기본값(false)을 돌려주는지,
        // 그 결과 GameSession의 기본 동작(힌트 노출 유지)이 안전한지 확인한다.
        assertTrue(GameSession.showListHint)

        GameSession.recordRoundPerformance(targetCount = 4, elapsedSeconds = 8f)

        assertTrue(GameSession.showListHint) // 모델 미초기화 → hideHint로 안 바뀜
    }

    @Test
    fun `registerWrongTap increments the wrong-tap counter and resets on advanceRound`() {
        GameSession.registerWrongTap()
        GameSession.registerWrongTap()
        assertEquals(2, GameSession.wrongTapsThisRound.value)

        GameSession.advanceRound()
        assertEquals(0, GameSession.wrongTapsThisRound.value)
    }

    @Test
    fun `recordRoundPerformance with zero targets does nothing`() {
        GameSession.recordRoundPerformance(targetCount = 0, elapsedSeconds = 5f)
        assertTrue(GameSession.showListHint)
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

    @Test
    fun `hasOnboarded defaults to false and flips true after completeOnboarding`() {
        assertFalse(GameSession.hasOnboarded.value)

        GameSession.completeOnboarding()

        assertTrue(GameSession.hasOnboarded.value)
    }

    @Test
    fun `settings toggles default on-on-off and flip via their setters`() {
        assertTrue(GameSession.autoDifficultyEnabled.value)
        assertTrue(GameSession.dualTaskEnabled.value)
        assertFalse(GameSession.alwaysShowListEnabled.value)

        GameSession.setAutoDifficulty(false)
        GameSession.setDualTask(false)
        GameSession.setAlwaysShowList(true)

        assertFalse(GameSession.autoDifficultyEnabled.value)
        assertFalse(GameSession.dualTaskEnabled.value)
        assertTrue(GameSession.alwaysShowListEnabled.value)
    }

    @Test
    fun `showListHint stays true when autoDifficulty is off, even after recording performance`() {
        GameSession.setAutoDifficulty(false)

        GameSession.recordRoundPerformance(targetCount = 4, elapsedSeconds = 1f)

        assertTrue(GameSession.showListHint)
    }

    @Test
    fun `showListHint stays true when alwaysShowList is on`() {
        GameSession.setAlwaysShowList(true)

        assertTrue(GameSession.showListHint)
    }
}
