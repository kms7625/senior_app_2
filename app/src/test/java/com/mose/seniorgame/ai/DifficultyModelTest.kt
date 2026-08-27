package com.mose.seniorgame.ai

import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * 실제 TFLite 추론(Interpreter)은 Android 런타임의 네이티브 라이브러리가 필요해
 * 이 JVM 단위 테스트로는 검증할 수 없다(계측 테스트 영역, docs/GDD.md TODO 참고).
 * 여기서는 "모델이 초기화되지 않았을 때 안전하게 동작하는가"만 확인한다 — 이 안전장치
 * 덕분에 GameSession은 초기화 실패/지연 시에도 크래시 없이 기본값으로 동작한다.
 */
class DifficultyModelTest {

    @Test
    fun `predictHideHint returns a safe default before initialize is called`() {
        assertFalse(DifficultyModel.predictHideHint(accuracy = 1f, reactionTimeSeconds = 0f, round = 10))
    }
}
