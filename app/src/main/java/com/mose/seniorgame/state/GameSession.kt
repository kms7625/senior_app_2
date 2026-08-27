package com.mose.seniorgame.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

/**
 * 화면 간 공유 게임 상태(MVP: 단일 액티비티 내 인메모리 싱글턴, 프로세스 종료 대응은
 * 아직 없음 — 온디바이스 AI 난이도 조절이 붙기 전 임시 구조).
 *
 * [showListHint]는 docs/GDD.md "③ 찾기(간격회상)" 규칙의 자리표시자 구현이다.
 * 실제 온디바이스 AI(TFLite) 난이도 조절이 들어오기 전까지는 라운드 수 기반의 단순
 * 규칙(1~2라운드는 오류배제학습으로 목록을 보여주고, 3라운드부터 회상만으로 진행)으로
 * 대체한다 — 이것 자체를 "온디바이스 AI 적용"이라고 제출 문서에 쓰면 안 된다
 * (senior-game-review 체크리스트: 가점은 실제 모델 추론에만 해당).
 */
object GameSession {
    var roundNumber = mutableStateOf(1)
        private set

    val selectedItems = mutableStateListOf<String>()

    val collectedItems = mutableStateListOf<String>()

    val showListHint: Boolean
        get() = roundNumber.value <= 2

    fun toggleSelected(item: String) {
        if (selectedItems.contains(item)) selectedItems.remove(item) else selectedItems.add(item)
    }

    fun toggleCollected(item: String) {
        if (collectedItems.contains(item)) collectedItems.remove(item) else collectedItems.add(item)
    }

    /** 결과 화면 도달 시 호출 — 다음 라운드로 넘어가며 이번 라운드 상태를 비운다. */
    fun advanceRound() {
        roundNumber.value += 1
        selectedItems.clear()
        collectedItems.clear()
    }
}
