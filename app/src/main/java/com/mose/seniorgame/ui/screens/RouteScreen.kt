package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val corners = listOf("채소 코너", "정육 코너", "유제품 코너", "계산대")

/**
 * 와이어프레임 03: 동선 계획.
 * 드래그 대신 순서 탭 방식 — 정밀 제스처 회피(senior-game-dev 접근성 기준).
 * 코너를 방문하고 싶은 순서대로 탭하면 번호가 붙고, 4곳 모두 정하면 확정 버튼이
 * 활성화된다.
 */
@Composable
fun RouteScreen(onConfirm: () -> Unit) {
    val order = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("방문할 순서대로 코너를 눌러주세요", style = MaterialTheme.typography.titleLarge)
        corners.forEach { corner ->
            val position = order.indexOf(corner)
            val label = if (position >= 0) "${position + 1}. $corner" else corner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (order.contains(corner)) order.remove(corner) else order.add(corner)
                    }
                    .padding(12.dp),
            ) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
            }
        }
        if (order.size == corners.size) {
            SeniorPrimaryButton(text = "동선 확정하기", onClick = onConfirm)
        } else {
            SeniorSecondaryButton(text = "코너 ${order.size}/${corners.size}개 선택됨", onClick = {})
        }
    }
}
