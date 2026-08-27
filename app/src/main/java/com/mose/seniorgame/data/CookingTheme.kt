package com.mose.seniorgame.data

/**
 * docs/theme-item-pool.md "요리 재료" 테마 표를 그대로 옮긴 것.
 * 목표 품목(target)과 그에 매칭된 미끼(decoy)를 함께 들고 있어, 매장 탐색 화면에서
 * 주의력 과제(미끼 사이에서 목표만 고르기)를 구성할 수 있다.
 */
object CookingTheme {
    /** 품목명 to (미끼, 가격원). 가격은 계산대(이중과제) 단계용 임시 시세. */
    val pairs: List<Triple<String, String, Int>> = listOf(
        Triple("두부", "순두부", 1500),
        Triple("계란", "메추리알", 4000),
        Triple("대파", "쪽파", 2000),
        Triple("마늘", "양파", 2500),
        Triple("라면", "국수", 4500),
        Triple("식용유", "참기름", 6000),
        Triple("설탕", "소금", 2000),
        Triple("우유", "두유", 2800),
        Triple("김", "미역", 3000),
        Triple("고추장", "된장", 5000),
    )

    /** 계획 세우기 화면에 제시할 후보. MVP는 앞쪽 [count]개를 고정으로 쓴다. */
    fun candidates(count: Int = 4): List<Pair<String, String>> =
        pairs.take(count).map { (target, decoy, _) -> target to decoy }

    fun decoyOf(target: String): String? = pairs.firstOrNull { it.first == target }?.second

    fun priceOf(target: String): Int = pairs.firstOrNull { it.first == target }?.third ?: 0

    fun totalPrice(items: List<String>): Int = items.sumOf { priceOf(it) }
}
