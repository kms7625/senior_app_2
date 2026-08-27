package com.mose.seniorgame.data

/**
 * docs/theme-item-pool.md "요리 재료" 테마 표를 그대로 옮긴 것.
 * 목표 품목(target)과 그에 매칭된 미끼(decoy)를 함께 들고 있어, 매장 탐색 화면에서
 * 주의력 과제(미끼 사이에서 목표만 고르기)를 구성할 수 있다.
 */
object CookingTheme {
    val pairs: List<Pair<String, String>> = listOf(
        "두부" to "순두부",
        "계란" to "메추리알",
        "대파" to "쪽파",
        "마늘" to "양파",
        "라면" to "국수",
        "식용유" to "참기름",
        "설탕" to "소금",
        "우유" to "두유",
        "김" to "미역",
        "고추장" to "된장",
    )

    /** 계획 세우기 화면에 제시할 후보. MVP는 앞쪽 [count]개를 고정으로 쓴다. */
    fun candidates(count: Int = 4): List<Pair<String, String>> = pairs.take(count)

    fun decoyOf(target: String): String? = pairs.firstOrNull { it.first == target }?.second
}
