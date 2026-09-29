package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.data.RoutePlan
import com.mose.seniorgame.state.GameSession

/**
 * 와이어프레임 03: 동선 계획.
 * 드래그 대신 순서 탭 방식 — 정밀 제스처 회피(senior-game-dev 접근성 기준).
 * [RoutePlan]의 2줄 격자 지도(윗줄 코너 3곳, 아랫줄 입구·계산대)에서 방문 순서대로
 * 탭하면 번호가 붙고, 4곳을 다 정하면 가장 짧은 동선과 비교한 한 줄 안내가 나온다.
 * 어떤 순서든 확정할 수 있다 — 평가는 알려주기만 하고 막지 않는다.
 */
@Composable
fun RouteScreen(onConfirm: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val sections = theme.sections
    val order = remember(theme.id) { mutableStateListOf<String>() }
    val toggle: (String) -> Unit = { stop -> if (order.contains(stop)) order.remove(stop) else order.add(stop) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("입구에서 출발해요. 들를 순서대로 눌러주세요", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            sections.forEach { MapCell(it, order.indexOf(it), onClick = { toggle(it) }) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MapCell("입구", -1, onClick = null)
            Box(Modifier.weight(1f))
            MapCell(RoutePlan.CHECKOUT, order.indexOf(RoutePlan.CHECKOUT), onClick = { toggle(RoutePlan.CHECKOUT) })
        }
        if (order.size == sections.size + 1) {
            Text(RoutePlan.feedback(order, sections), style = MaterialTheme.typography.bodyLarge)
            SeniorPrimaryButton(text = "동선 확정하기", onClick = onConfirm)
        } else {
            Text("${order.size}/${sections.size + 1}곳 정했어요", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

/** 지도 한 칸. [onClick]이 null이면 누를 수 없는 표지(입구). */
@Composable
private fun RowScope.MapCell(label: String, position: Int, onClick: (() -> Unit)?) {
    val picked = position >= 0
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (picked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .weight(1f)
            .height(120.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(if (picked) "${position + 1}. $label" else label, style = MaterialTheme.typography.titleLarge)
        }
    }
}
