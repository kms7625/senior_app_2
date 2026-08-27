package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 와이어프레임 05: 계산대(선택).
 * 순수 인지 이중과제(암산) — 신체운동 결합 dual-task 문헌과는 별개로
 * PMC12726003(Breakfast Game)의 분할주의 훈련 근거를 따른다(docs/GDD.md 참고).
 * 설정에서 이중과제를 끄면 이 화면 자체가 스킵된다(MainActivity 라우팅 확장 예정).
 */
@Composable
fun CheckoutScreen(onConfirm: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("예산 10,000원 — 암산으로 맞춰보세요", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SeniorSecondaryButton(text = "이 단계 건너뛰기", onClick = onSkip)
            SeniorPrimaryButton(text = "확인", onClick = onConfirm)
        }
    }
}
