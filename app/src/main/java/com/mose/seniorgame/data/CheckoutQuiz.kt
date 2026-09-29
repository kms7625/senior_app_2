package com.mose.seniorgame.data

import kotlin.random.Random

/**
 * 계산대 암산 문제의 보기 3개를 만든다. 정답(합계) 1개 + 오답 2개.
 *
 * 오답은 정답에서 500원·1000원 단위로 벌어지게 만든다 — 100원 단위 차이는
 * 시니어에게 변별이 과하게 어렵고, 너무 크게 벌어지면 암산 없이 찍어도 맞힌다.
 * 전부 0원 초과, 서로 다른 값이다.
 */
object CheckoutQuiz {

    private val OFFSETS = listOf(-1000, -500, 500, 1000)

    fun choices(total: Int, random: Random = Random.Default): List<Int> {
        val wrong = OFFSETS.map { total + it }.filter { it > 0 }.shuffled(random).take(2)
        return (wrong + total).shuffled(random)
    }
}
