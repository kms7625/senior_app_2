package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.state.GameSession

/**
 * 와이어프레임 02: 계획 세우기.
 * 캐릭터는 제안만, 선택 체크는 플레이어가 직접 — "우리 집에 놀러와" 서사의 주체성 원칙
 * (docs/GDD.md 참고). 선택 결과는 [GameSession.selectedItems]에 저장되어
 * SearchScreen의 목표 품목 그대로 이어진다.
 */
@Composable
fun PlanScreen(onConfirm: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val candidates = theme.candidates()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("${theme.label} · 무엇이 필요한지 골라주세요", style = MaterialTheme.typography.titleLarge)
        candidates.forEach { (target, _) ->
            // Row 전체를 탭 영역으로 — 체크박스만 노려 눌러야 하는 부담을 줄인다
            // (48dp 최소 터치 타겟 원칙, senior-game-dev 접근성 기준).
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { GameSession.toggleSelected(target) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = GameSession.selectedItems.contains(target),
                    onCheckedChange = null,
                )
                Text(target, style = MaterialTheme.typography.bodyLarge)
            }
        }
        SeniorPrimaryButton(
            text = "목록 확정하기 (${GameSession.selectedItems.size}개 선택)",
            onClick = onConfirm,
        )
    }
}
