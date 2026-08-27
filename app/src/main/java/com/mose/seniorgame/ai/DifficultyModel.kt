package com.mose.seniorgame.ai

import android.content.Context
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.min
import org.tensorflow.lite.Interpreter

/**
 * 라운드 성과를 보고 다음 라운드의 힌트 노출 여부(=난이도)를 정하는 온디바이스 AI.
 * `tools/train_difficulty_model.py`로 로컬에서 학습·변환한 `assets/difficulty_model.tflite`를
 * 기기 내부에서만 추론한다 — 네트워크 호출 없음(기획안 13.1/13.2 준수).
 *
 * ⚠️ 아래 3개 입력 특성의 정규화 방식은 학습 스크립트와 반드시 동일해야 한다:
 *   1. accuracy         — 0~1 그대로
 *   2. reactionTimeNorm — 초 단위 반응시간 / 10, [0,1]로 clamp
 *   3. roundNorm         — 라운드 번호 / 10, [0,1]로 clamp
 */
object DifficultyModel {
    private var interpreter: Interpreter? = null

    fun initialize(context: Context) {
        if (interpreter != null) return
        interpreter = Interpreter(loadModelFile(context))
    }

    /**
     * @return true면 다음 라운드에서 힌트를 숨긴다(간격회상만으로 진행). 모델이 아직
     * 초기화되지 않았으면 안전한 기본값(false = 힌트 유지)을 돌려준다.
     */
    fun predictHideHint(accuracy: Float, reactionTimeSeconds: Float, round: Int): Boolean {
        val interp = interpreter ?: return false
        val input = arrayOf(
            floatArrayOf(
                accuracy.coerceIn(0f, 1f),
                min(reactionTimeSeconds / 10f, 1f),
                min(round / 10f, 1f),
            ),
        )
        val output = Array(1) { FloatArray(1) }
        interp.run(input, output)
        return output[0][0] > 0.5f
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val afd = context.assets.openFd("difficulty_model.tflite")
        FileInputStream(afd.fileDescriptor).use { stream ->
            return stream.channel.map(
                FileChannel.MapMode.READ_ONLY,
                afd.startOffset,
                afd.declaredLength,
            )
        }
    }
}
