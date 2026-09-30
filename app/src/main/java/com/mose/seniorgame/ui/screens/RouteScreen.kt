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
 * 탭하면 번호가 붙는다. 고른 물건이 있는 코너만 들르면 되고 필요 없는 코너는 건너뛴다.
 * 계산대를 고르면 한 줄 평가(안 들러도 되는 코너·빠진 코너·더 짧은 길)가 나온다.
 * 안 들러도 되는 코너가 들어 있으면 확정 버튼이 비활성화되고, 나머지 평가는 알려주기만 한다.
 */
@Composable
fun RouteScreen(onConfirm: () -> Unit) {
    val theme = GameSession.currentTheme.value
    val sections = theme.sections
    val order = remember(theme.id) { mutableStateListOf<String>() }
    val toggle: (String) -> Unit = { stop -> if (order.contains(stop)) order.remove(stop) else order.add(stop) }
    val picked = GameSession.selectedItems.toList()
    val itemCorners = picked.mapNotNull { item -> theme.sectionOf(item)?.let { item to it } }.toMap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("입구에서 출발해요. 필요한 코너만 순서대로 눌러주세요", style = MaterialTheme.typography.titleLarge)
        Text("살 물건: ${picked.joinToString(", ")} · 계산대는 꼭 들러요", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            sections.forEach { MapCell(it, order.indexOf(it), onClick = { toggle(it) }) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MapCell("입구", -1, onClick = null)
            Box(Modifier.weight(1f))
            MapCell(RoutePlan.CHECKOUT, order.indexOf(RoutePlan.CHECKOUT), onClick = { toggle(RoutePlan.CHECKOUT) })
        }
        if (RoutePlan.CHECKOUT in order) {
            Text(RoutePlan.feedback(order, sections, itemCorners), style = MaterialTheme.typography.bodyLarge)
            SeniorPrimaryButton(
                text = "동선 확정하기",
                onClick = onConfirm,
                enabled = RoutePlan.unneeded(order, sections, itemCorners).isEmpty(),
            )
        } else {
            Text("${order.size}곳 골랐어요 · 마지막에 계산대를 눌러주세요", style = MaterialTheme.typography.bodyLarge)
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
