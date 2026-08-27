package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 와이어프레임 04: 매장 탐색(찾기).
 * 목표 품목 + 미끼(decoy)는 docs/theme-item-pool.md "요리 재료" 테마에서 발췌.
 * 초반 라운드는 목록 다시 보기 허용(오류배제학습), 라운드가 쌓일수록 회상만으로
 * 진행(간격회상) — 이 로직은 다음 단계에서 실제 상태로 구현한다.
 */
@Composable
fun SearchScreen(onDone: () -> Unit) {
    val items = listOf("두부" to true, "순두부" to false, "대파" to true, "쪽파" to false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("채소 코너 · 목록 다시 보기", style = MaterialTheme.typography.bodyLarge)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize().weight(1f, fill = false),
        ) {
            items(items) { (name, isTarget) ->
                Text(
                    text = if (isTarget) "$name ✓" else name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
        SeniorPrimaryButton(text = "다 담았어요", onClick = onDone)
    }
}
