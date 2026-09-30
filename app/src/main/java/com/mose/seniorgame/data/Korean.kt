package com.mose.seniorgame.data

/** "순두부" → "순두부는", "쪽파" → "쪽파는", "미역" → "미역은" (받침 유무로 조사 선택). */
internal fun withTopicParticle(word: String): String {
    val last = word.lastOrNull() ?: return word
    val hasFinal = last in '가'..'힣' && (last - '가') % 28 != 0
    return word + if (hasFinal) "은" else "는"
}
