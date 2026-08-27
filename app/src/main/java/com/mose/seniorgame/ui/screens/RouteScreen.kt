package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 와이어프레임 03: 동선 계획.
 * 드래그 대신 순서 탭 방식 — 정밀 제스처 회피(senior-game-dev 접근성 기준).
 * 코너 탭 인터랙션은 이후 이슈에서 실제 지도 컴포넌트로 확장한다.
 */
@Composable
fun RouteScreen(onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("방문할 순서대로 코너를 눌러주세요", style = MaterialTheme.typography.titleLarge)
        Text("채소 코너 → 정육 코너 → 유제품 코너 → 계산대", style = MaterialTheme.typography.bodyLarge)
        SeniorPrimaryButton(text = "동선 확정하기", onClick = onConfirm)
    }
}
