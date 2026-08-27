package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.data.CookingTheme
import com.mose.seniorgame.state.GameSession

/**
 * 와이어프레임 04: 매장 탐색(찾기).
 * 목표 품목은 PlanScreen에서 고른 [GameSession.selectedItems] 그대로, 각 품목 옆에
 * 미끼(decoy)를 하나씩 섞어 주의력 과제를 구성한다.
 *
 * 간격회상 적용: [GameSession.showListHint]가 true인 1~2라운드는 목표 품목에 정답
 * 표시(✓)를 남겨 오류배제학습으로 진행하고, 3라운드부터는 표시를 지워 회상만으로
 * 찾게 한다(docs/GDD.md "③ 찾기" 참고).
 */
@Composable
fun SearchScreen(onDone: () -> Unit) {
    val targets = GameSession.selectedItems
    val tiles = remember(targets.toList()) {
        val decoys = targets.mapNotNull { CookingTheme.decoyOf(it) }
        (targets + decoys).shuffled()
    }
    val showHint = GameSession.showListHint
    val allCollected = targets.isNotEmpty() && targets.all { GameSession.collectedItems.contains(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (showHint) "채소 코너 · 목표 품목에는 ✓ 표시가 있어요" else "채소 코너 · 목록을 떠올려서 찾아보세요",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "담은 물건 ${GameSession.collectedItems.size}/${targets.size}",
            style = MaterialTheme.typography.bodyLarge,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxSize().weight(1f, fill = false),
        ) {
            items(tiles) { name ->
                val isTarget = targets.contains(name)
                val isCollected = GameSession.collectedItems.contains(name)
                val label = when {
                    isTarget && showHint && isCollected -> "$name ✓"
                    isTarget && showHint -> "$name ·"
                    isCollected -> "$name ✓"
                    else -> name
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(enabled = isTarget) { GameSession.toggleCollected(name) },
                )
            }
        }
        if (allCollected) {
            SeniorPrimaryButton(text = "다 담았어요", onClick = onDone)
        } else {
            SeniorSecondaryButton(
                text = "남은 물건 ${targets.size - GameSession.collectedItems.size}개",
                onClick = {},
            )
        }
    }
}
