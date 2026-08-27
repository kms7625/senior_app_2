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
import com.mose.seniorgame.data.CookingTheme
import com.mose.seniorgame.state.GameSession

private const val BUDGET = 10_000

/**
 * 와이어프레임 05: 계산대(선택).
 * 순수 인지 이중과제(암산) — 신체운동 결합 dual-task 문헌과는 별개로
 * PMC12726003(Breakfast Game)의 분할주의 훈련 근거를 따른다(docs/GDD.md 참고).
 * 예산 초과여도 진행을 막지 않는다 — 실패 부담을 줄이고 정보만 준다(학생안내 원칙).
 */
@Composable
fun CheckoutScreen(onConfirm: () -> Unit, onSkip: () -> Unit) {
    val total = CookingTheme.totalPrice(GameSession.selectedItems)
    val withinBudget = total <= BUDGET
    val message = if (withinBudget) {
        "합계 ${total}원 · 예산 ${BUDGET}원 안에 잘 맞췄어요"
    } else {
        "합계 ${total}원 · 예산보다 ${total - BUDGET}원 더 썼어요, 괜찮아요"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("예산 ${BUDGET}원 — 암산으로 맞춰보세요", style = MaterialTheme.typography.titleLarge)
        Text(message, style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SeniorSecondaryButton(text = "이 단계 건너뛰기", onClick = onSkip)
            SeniorPrimaryButton(text = "확인", onClick = onConfirm)
        }
    }
}
