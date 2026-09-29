package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 *
 * 테마의 품목 10개를 전부 후보로 보여준다 — 예전엔 앞 4개만 고정으로 보여줘서 나머지
 * 6개는 절대 선택할 수 없었다(코드 리뷰에서 발견, 2026-08-27 수정). "몇 개를 보여줄지"
 * 같은 숨은 알고리즘보다, 전체를 보여주고 플레이어가 자유롭게 고르는 쪽이
 * "우리 집에 놀러와"의 주체성 원칙에도 더 맞는다.
 */
@Composable
fun PlanScreen(onConfirm: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val candidates = theme.items

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("${theme.label} · 무엇이 필요한지 골라주세요", style = MaterialTheme.typography.titleLarge)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            candidates.forEach { item ->
                // Row 전체를 탭 영역으로 — 체크박스만 노려 눌러야 하는 부담을 줄인다
                // (48dp 최소 터치 타겟 원칙, senior-game-dev 접근성 기준).
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { GameSession.toggleSelected(item.name) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Checkbox(
                        checked = GameSession.selectedItems.contains(item.name),
                        onCheckedChange = null,
                    )
                    ItemIcon(drawableName = "ic_item_${item.iconSlug}", modifier = Modifier.size(56.dp))
                    Text(item.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        SeniorPrimaryButton(
            text = "목록 확정하기 (${GameSession.selectedItems.size}개 선택)",
            onClick = onConfirm,
        )
    }
}
