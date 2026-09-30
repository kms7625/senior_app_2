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
 * 칸 사이 이동 거리는 가로+세로 칸 수(맨해튼 거리)로 센다. 고른 물건이 있는 코너만
 * 들르면 되고(필요 없는 코너는 건너뜀), 그 코너들의 가장 짧은 동선과 플레이어 순서를
 * 비교해 알려주기만 한다 — 진행을 막지는 않는다.
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

    /** [stops] 코너들을 모두 들르고 계산대를 마지막에 두는 순서 중 이동 거리가 가장 짧은 것. */
    fun shortest(stops: List<String>, sections: List<String>): List<String> =
        permutations(stops).map { it + CHECKOUT }.minBy { distance(it, sections) }

    /** 고른 순서 중 이번에 살 물건이 없는 코너(지도 순서). 하나라도 있으면 동선을 확정할 수 없다. */
    fun unneeded(order: List<String>, sections: List<String>, itemCorners: Map<String, String>): List<String> =
        sections.filter { it in order && it !in itemCorners.values }

    /** 필요한 코너를 전부, 필요 없는 코너는 하나도 없이 고르고 계산대까지 골랐을 때만 확정할 수 있다. */
    fun canConfirm(order: List<String>, sections: List<String>, itemCorners: Map<String, String>): Boolean =
        CHECKOUT in order &&
            unneeded(order, sections, itemCorners).isEmpty() &&
            itemCorners.values.all { it in order }

    /**
     * 동선 평가 한 줄. [itemCorners]는 고른 물건 → 그 물건이 있는 코너.
     * 우선순위: 안 들러도 되는 코너 → 빠진 코너(둘 다 확정 불가) → 계산대 위치 → 거리.
     */
    fun feedback(order: List<String>, sections: List<String>, itemCorners: Map<String, String>): String {
        val needed = sections.filter { it in itemCorners.values }
        val extra = unneeded(order, sections, itemCorners)
        val missing = itemCorners.entries.firstOrNull { it.value !in order }
        val best = shortest(needed, sections)
        return when {
            extra.isNotEmpty() ->
                "${withTopicParticle(extra.joinToString("·"))} 이번엔 안 들러도 돼요. 다시 눌러서 빼 주세요"
            missing != null ->
                "${withTopicParticle(missing.key)} ${missing.value}에 있어요. 그 코너도 들러 주세요"
            order.last() != CHECKOUT ->
                "계산대는 마지막에 들르면 계산한 뒤 다시 장을 보러 가지 않아도 돼요"
            distance(order, sections) == distance(best, sections) ->
                "가장 덜 걷는 길을 찾으셨어요!"
            else ->
                "좋아요! ${best.joinToString(" → ")} 순서로 가면 조금 덜 걸어요"
        }
    }

    private fun permutations(items: List<String>): List<List<String>> =
        if (items.size <= 1) listOf(items)
        else items.flatMap { head -> permutations(items - head).map { listOf(head) + it } }
}
