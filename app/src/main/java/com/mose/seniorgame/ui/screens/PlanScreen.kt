package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 와이어프레임 02: 계획 세우기.
 * 캐릭터는 제안만, 선택 체크는 플레이어가 직접 — "우리 집에 놀러와" 서사의 주체성 원칙
 * (docs/GDD.md 참고). 품목은 docs/theme-item-pool.md의 "요리 재료" 테마 일부 예시.
 */
@Composable
fun PlanScreen(onConfirm: () -> Unit) {
    val candidates = listOf("두부", "계란", "대파", "마늘")
    val checked = remember { mutableStateMapOfDefault(candidates) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("냉장고 속 부족한 것을 골라주세요", style = MaterialTheme.typography.titleLarge)
        candidates.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked[item] == true,
                    onCheckedChange = { checked[item] = it },
                )
                Text(item, style = MaterialTheme.typography.bodyLarge)
            }
        }
        SeniorPrimaryButton(text = "목록 확정하기", onClick = onConfirm)
    }
}

private fun mutableStateMapOfDefault(keys: List<String>): androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean> {
    val map = androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean>()
    keys.forEachIndexed { i, k -> map[k] = i < 2 }
    return map
}
