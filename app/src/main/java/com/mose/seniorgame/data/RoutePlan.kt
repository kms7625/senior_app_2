package com.mose.seniorgame.data

import kotlin.math.abs

/**
 * 동선 계획 화면의 매장 지도와 동선 평가.
 *
 * 지도는 2줄 3칸 격자다. 윗줄에 코너 3곳, 아랫줄 왼쪽이 입구, 오른쪽이 계산대:
 * ```
 * [코너0] [코너1] [코너2]
 * [입구 ]         [계산대]
 * ```
 * 칸 사이 이동 거리는 가로+세로 칸 수(맨해튼 거리)로 센다. 가장 짧은 동선을
 * 계산해 플레이어가 고른 순서와 비교만 하고, 진행을 막지는 않는다.
 */
object RoutePlan {

    const val CHECKOUT = "계산대"

    private data class Cell(val col: Int, val row: Int)

    private val ENTRANCE = Cell(0, 1)
    private val CHECKOUT_CELL = Cell(2, 1)

    private fun cellOf(stop: String, sections: List<String>): Cell =
        if (stop == CHECKOUT) CHECKOUT_CELL else Cell(sections.indexOf(stop), 0)

    fun distance(order: List<String>, sections: List<String>): Int {
        var here = ENTRANCE
        var total = 0
        order.forEach { stop ->
            val next = cellOf(stop, sections)
            total += abs(here.col - next.col) + abs(here.row - next.row)
            here = next
        }
        return total
    }

    /** 계산대를 마지막에 두는 순서 중 이동 거리가 가장 짧은 것. */
    fun shortest(sections: List<String>): List<String> =
        permutations(sections).map { it + CHECKOUT }.minBy { distance(it, sections) }

    fun feedback(order: List<String>, sections: List<String>): String = when {
        order.last() != CHECKOUT ->
            "계산대는 마지막에 들르면 계산한 뒤 다시 장을 보러 가지 않아도 돼요"
        distance(order, sections) == distance(shortest(sections), sections) ->
            "가장 덜 걷는 길을 찾으셨어요!"
        else ->
            "좋아요! ${shortest(sections).joinToString(" → ")} 순서로 가면 조금 덜 걸어요"
    }

    private fun permutations(items: List<String>): List<List<String>> =
        if (items.size <= 1) listOf(items)
        else items.flatMap { head -> permutations(items - head).map { listOf(head) + it } }
}
