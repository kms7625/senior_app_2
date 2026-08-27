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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.state.GameSession

/**
 * 와이어프레임 04: 매장 탐색(찾기).
 * 목표 품목은 PlanScreen에서 고른 [GameSession.selectedItems] 그대로, 각 품목 옆에
 * 미끼(decoy)를 하나씩 섞어 주의력 과제를 구성한다. 미끼도 탭할 수 있어야 "잘못
 * 골랐다"를 감지할 수 있으므로 전부 클릭 가능하게 둔다.
 *
 * 간격회상 적용: [GameSession.showListHint]는 온디바이스 AI(TFLite)가 직전 라운드
 * 정답률·반응시간으로 정한 값이다(docs/GDD.md "③ 찾기" 참고). 목표를 전부 모으면
 * 이번 라운드 정답률·소요시간을 [GameSession.recordRoundPerformance]로 넘겨 다음
 * 라운드 난이도를 갱신한다.
 */
@Composable
fun SearchScreen(onDone: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val targets = GameSession.selectedItems
    val tiles = remember(theme.id, targets.toList()) {
        val decoys = targets.mapNotNull { theme.decoyOf(it) }
        (targets + decoys).shuffled()
    }
    val startTimeMillis = remember(theme.id, targets.toList()) { System.currentTimeMillis() }
    val warnedDecoys = remember(theme.id, targets.toList()) { mutableStateListOf<String>() }

    val showHint = GameSession.showListHint
    val allCollected = targets.isNotEmpty() && targets.all { GameSession.collectedItems.contains(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (showHint) "${theme.label} 코너 · 목표 품목에는 ✓ 표시가 있어요" else "${theme.label} 코너 · 목록을 떠올려서 찾아보세요",
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
                // 미끼는 아직 전용 ShoppingItem이 없어 iconSlug가 없다 — 자리표시 그림으로
                // 대체(ItemIcon이 못 찾는 slug는 자동으로 플레이스홀더를 보여준다).
                val iconSlug = theme.itemOf(name)?.iconSlug ?: "decoy_placeholder"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            if (isTarget) {
                                GameSession.toggleCollected(name)
                            } else if (!warnedDecoys.contains(name)) {
                                // 같은 미끼를 반복 탭해도 한 번만 오답으로 센다.
                                warnedDecoys.add(name)
                                GameSession.registerWrongTap()
                            }
                        },
                ) {
                    ItemIcon(iconSlug = iconSlug)
                    Text(text = label, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        if (allCollected) {
            SeniorPrimaryButton(
                text = "다 담았어요",
                onClick = {
                    val elapsedSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000f
                    GameSession.recordRoundPerformance(targets.size, elapsedSeconds)
                    onDone()
                },
            )
        } else {
            SeniorSecondaryButton(
                text = "남은 물건 ${targets.size - GameSession.collectedItems.size}개",
                onClick = {},
            )
        }
    }
}
