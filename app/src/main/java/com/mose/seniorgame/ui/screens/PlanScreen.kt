package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        // 품목 10개를 카드 5열 격자로 — 카드 전체가 탭 영역(48dp 이상), 고른 카드는
        // 초록 테두리·배경 + "✓ 골랐어요" 글자로 함께 표시한다.
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(candidates) { item ->
                val picked = GameSession.selectedItems.contains(item.name)
                SeniorCard(onClick = { GameSession.toggleSelected(item.name) }, selected = picked) {
                    ItemIcon(drawableName = "ic_item_${item.iconSlug}", modifier = Modifier.size(96.dp))
                    Text(item.name, style = MaterialTheme.typography.bodyLarge)
                    Text(if (picked) "✓ 골랐어요" else " ", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        SeniorPrimaryButton(
            text = "목록 확정하기 (${GameSession.selectedItems.size}개 선택)",
            onClick = onConfirm,
        )
    }
}
