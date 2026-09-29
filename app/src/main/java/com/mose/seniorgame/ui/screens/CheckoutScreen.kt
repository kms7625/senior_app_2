package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.data.CheckoutQuiz
import com.mose.seniorgame.state.GameSession

private const val BUDGET = 10_000

/**
 * 와이어프레임 05: 계산대(선택).
 * 순수 인지 이중과제(암산) — 신체운동 결합 dual-task 문헌과는 별개로
 * PMC12726003(Breakfast Game)의 분할주의 훈련 근거를 따른다(docs/GDD.md 참고).
 * 담은 물건의 가격을 보고 합계를 보기 3개 중에서 고른다. 틀려도 감점 없이 다시
 * 고를 수 있고, 예산 초과여도 진행을 막지 않는다(학생안내 "실패 부담 최소화" 원칙).
 */
@Composable
fun CheckoutScreen(onConfirm: () -> Unit, onSkip: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val items = GameSession.selectedItems.toList()
    val total = theme.totalPrice(items)
    val choices = remember(theme.id, items) { CheckoutQuiz.choices(total) }
    var wrongPick by remember(theme.id, items) { mutableStateOf<Int?>(null) }
    var solved by remember(theme.id, items) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("담은 물건은 모두 얼마일까요?", style = MaterialTheme.typography.titleLarge)
        items.forEach { name ->
            Text("$name  ${theme.priceOf(name)}원", style = MaterialTheme.typography.bodyLarge)
        }

        if (!solved) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                choices.forEach { amount ->
                    SeniorSecondaryButton(
                        text = "${amount}원",
                        onClick = { if (amount == total) solved = true else wrongPick = amount },
                    )
                }
            }
            wrongPick?.let {
                Text("${it}원은 아니에요. 천천히 한 번 더 더해볼까요?", style = MaterialTheme.typography.bodyLarge)
            }
            SeniorSecondaryButton(text = "이 단계 건너뛰기", onClick = onSkip)
        } else {
            val budgetMessage = if (total <= BUDGET) {
                "맞아요, ${total}원이에요! 예산 ${BUDGET}원 안에서 잘 준비했어요"
            } else {
                "맞아요, ${total}원이에요! 예산보다 ${total - BUDGET}원 더 썼지만 괜찮아요"
            }
            Text(budgetMessage, style = MaterialTheme.typography.bodyLarge)
            SeniorPrimaryButton(text = "확인", onClick = onConfirm)
        }
    }
}
