package com.mose.seniorgame.state

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mose.seniorgame.ai.DifficultyModel
import com.mose.seniorgame.data.ShoppingTheme
import com.mose.seniorgame.data.ThemePool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.gameDataStore by preferencesDataStore(name = "game_session")

/**
 * 화면 간 공유 게임 상태(MVP: 단일 액티비티 내 인메모리 싱글턴, DataStore로 영구
 * 저장). 온보딩 여부·선호 테마뿐 아니라 라운드 진행 상태(선택/수집 품목, 라운드
 * 수, 오답 수, 다음 라운드 난이도)까지 전부 저장해서 프로세스가 죽어도 하던 라운드
 * 그대로 이어할 수 있다([initializePersistence] 참고). 저장은 상태가 바뀔 때마다
 * [persist]를 호출하는 방식(값이 몇 개 안 돼 매번 전체를 다시 쓴다 — 항목이 크게
 * 늘어나면 부분 갱신으로 바꾸는 걸 재검토).
 *
 * [showListHint]는 온디바이스 AI(TFLite, [DifficultyModel])가 직전 라운드 정답률·
 * 반응시간·라운드 수를 보고 추론한 결과를 따른다(docs/GDD.md "③ 찾기(간격회상)" 참고).
 * 아직 라운드를 한 번도 마치지 않은 1라운드는 추론할 데이터가 없으므로 기본값(힌트
 * 노출)으로 시작한다.
 */
object GameSession {
    private val hasOnboardedKey = booleanPreferencesKey("has_onboarded")
    private val themeIdKey = stringPreferencesKey("theme_id")
    private val roundNumberKey = intPreferencesKey("round_number")
    private val selectedItemsKey = stringSetPreferencesKey("selected_items")
    private val collectedItemsKey = stringSetPreferencesKey("collected_items")
    private val wrongTapsKey = intPreferencesKey("wrong_taps_this_round")
    private val hideHintKey = booleanPreferencesKey("hide_hint_next_round")

    private var dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>? = null
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var roundNumber = mutableStateOf(1)
        private set

    /** 온보딩/설정에서 고른 선호 테마. 기본값은 요리 재료(가장 먼저 코드화된 테마). */
    var currentTheme = mutableStateOf(ThemePool.cooking)
        private set

    /** 첫 실행 온보딩(테마 선택)을 마쳤는지. [initializePersistence] 호출 전엔 false. */
    var hasOnboarded = mutableStateOf(false)
        private set

    val selectedItems = mutableStateListOf<String>()

    val collectedItems = mutableStateListOf<String>()

    /** 이번 라운드에 미끼(decoy)를 잘못 골라 탭한 횟수 — 정답률 계산에 쓰인다. */
    var wrongTapsThisRound = mutableStateOf(0)
        private set

    private var hideHintNextRound = mutableStateOf(false)

    val showListHint: Boolean
        get() = !hideHintNextRound.value

    /**
     * 저장된 상태를 전부 동기적으로 읽어온다. MainActivity.onCreate에서
     * setContent보다 먼저 호출해야 첫 렌더링부터 정확한 시작 화면(홈 vs 온보딩)과
     * 하던 라운드 상태를 바로 복원할 수 있다. 저장 데이터가 작아 runBlocking으로도
     * 체감 지연이 거의 없다고 판단해 별도 스플래시 화면 없이 동기 처리했다 —
     * 항목이 크게 늘어나면 재검토.
     */
    fun initializePersistence(context: Context) {
        val store = context.applicationContext.gameDataStore
        dataStore = store
        val prefs = runBlocking { store.data.first() }
        hasOnboarded.value = prefs[hasOnboardedKey] ?: false
        prefs[themeIdKey]?.let { currentTheme.value = ThemePool.byId(it) }
        roundNumber.value = prefs[roundNumberKey] ?: 1
        selectedItems.clear()
        selectedItems.addAll(prefs[selectedItemsKey] ?: emptySet())
        collectedItems.clear()
        collectedItems.addAll(prefs[collectedItemsKey] ?: emptySet())
        wrongTapsThisRound.value = prefs[wrongTapsKey] ?: 0
        hideHintNextRound.value = prefs[hideHintKey] ?: false
    }

    /** 상태가 바뀔 때마다 호출 — 다음 실행에서도 이어할 수 있도록 전체를 다시 쓴다. */
    private fun persist() {
        val store = dataStore ?: return
        val onboarded = hasOnboarded.value
        val themeId = currentTheme.value.id
        val round = roundNumber.value
        val selected = selectedItems.toSet()
        val collected = collectedItems.toSet()
        val wrongTaps = wrongTapsThisRound.value
        val hideHint = hideHintNextRound.value
        ioScope.launch {
            store.edit { prefs ->
                prefs[hasOnboardedKey] = onboarded
                prefs[themeIdKey] = themeId
                prefs[roundNumberKey] = round
                prefs[selectedItemsKey] = selected
                prefs[collectedItemsKey] = collected
                prefs[wrongTapsKey] = wrongTaps
                prefs[hideHintKey] = hideHint
            }
        }
    }

    fun selectTheme(theme: ShoppingTheme) {
        currentTheme.value = theme
        selectedItems.clear()
        collectedItems.clear()
        persist()
    }

    fun completeOnboarding() {
        hasOnboarded.value = true
        persist()
    }

    fun toggleSelected(item: String) {
        if (selectedItems.contains(item)) selectedItems.remove(item) else selectedItems.add(item)
        persist()
    }

    fun toggleCollected(item: String) {
        if (collectedItems.contains(item)) collectedItems.remove(item) else collectedItems.add(item)
        persist()
    }

    fun registerWrongTap() {
        wrongTapsThisRound.value += 1
        persist()
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
        persist()
    }

    /** 결과 화면 도달 시 호출 — 다음 라운드로 넘어가며 이번 라운드 상태를 비운다. */
    fun advanceRound() {
        roundNumber.value += 1
        selectedItems.clear()
        collectedItems.clear()
        wrongTapsThisRound.value = 0
        persist()
    }

    /** 싱글턴이라 테스트마다 초기 상태로 되돌리기 위한 용도. 앱 코드에서는 쓰지 않는다. */
    fun resetForTest() {
        roundNumber.value = 1
        currentTheme.value = ThemePool.cooking
        hasOnboarded.value = false
        selectedItems.clear()
        collectedItems.clear()
        wrongTapsThisRound.value = 0
        hideHintNextRound.value = false
    }
}
