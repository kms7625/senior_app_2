package com.mose.seniorgame.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.mose.seniorgame.ai.DifficultyModel
import com.mose.seniorgame.data.ShoppingTheme
import com.mose.seniorgame.data.ThemePool

/**
 * 화면 간 공유 게임 상태(MVP: 단일 액티비티 내 인메모리 싱글턴, 프로세스 종료 대응은
 * 아직 없음).
 *
 * [showListHint]는 온디바이스 AI(TFLite, [DifficultyModel])가 직전 라운드 정답률·
 * 반응시간·라운드 수를 보고 추론한 결과를 따른다(docs/GDD.md "③ 찾기(간격회상)" 참고).
 * 아직 라운드를 한 번도 마치지 않은 1라운드는 추론할 데이터가 없으므로 기본값(힌트
 * 노출)으로 시작한다.
 */
object GameSession {
    var roundNumber = mutableStateOf(1)
        private set

    /** 온보딩/설정에서 고른 선호 테마. 기본값은 요리 재료(가장 먼저 코드화된 테마). */
    var currentTheme = mutableStateOf(ThemePool.cooking)
        private set

    val selectedItems = mutableStateListOf<String>()

    val collectedItems = mutableStateListOf<String>()

    /** 이번 라운드에 미끼(decoy)를 잘못 골라 탭한 횟수 — 정답률 계산에 쓰인다. */
    var wrongTapsThisRound = mutableStateOf(0)
        private set

    private var hideHintNextRound = mutableStateOf(false)

    val showListHint: Boolean
        get() = !hideHintNextRound.value

    fun selectTheme(theme: ShoppingTheme) {
        currentTheme.value = theme
        selectedItems.clear()
        collectedItems.clear()
    }

    fun toggleSelected(item: String) {
        if (selectedItems.contains(item)) selectedItems.remove(item) else selectedItems.add(item)
    }

    fun toggleCollected(item: String) {
        if (collectedItems.contains(item)) collectedItems.remove(item) else collectedItems.add(item)
    }

    fun registerWrongTap() {
        wrongTapsThisRound.value += 1
    }

    /**
     * 매장 탐색 화면에서 목표 품목을 전부 모았을 때 호출한다. 이번 라운드 정답률과
     * 품목당 평균 반응시간을 온디바이스 AI에 넘겨 다음 라운드 힌트 노출 여부를 정한다.
     */
    fun recordRoundPerformance(targetCount: Int, elapsedSeconds: Float) {
        if (targetCount <= 0) return
        val accuracy = targetCount.toFloat() / (targetCount + wrongTapsThisRound.value)
        val avgReactionTime = elapsedSeconds / targetCount
        hideHintNextRound.value = DifficultyModel.predictHideHint(
            accuracy = accuracy,
            reactionTimeSeconds = avgReactionTime,
            round = roundNumber.value,
        )
    }

    /** 결과 화면 도달 시 호출 — 다음 라운드로 넘어가며 이번 라운드 상태를 비운다. */
    fun advanceRound() {
        roundNumber.value += 1
        selectedItems.clear()
        collectedItems.clear()
        wrongTapsThisRound.value = 0
    }

    /** 싱글턴이라 테스트마다 초기 상태로 되돌리기 위한 용도. 앱 코드에서는 쓰지 않는다. */
    fun resetForTest() {
        roundNumber.value = 1
        currentTheme.value = ThemePool.cooking
        selectedItems.clear()
        collectedItems.clear()
        wrongTapsThisRound.value = 0
        hideHintNextRound.value = false
    }
}
