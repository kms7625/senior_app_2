package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 웹 리서치 근거(Android Accessibility Help, Material Design 접근성 가이드) 기준
// 터치 타겟 최소 48dp — senior-game-dev/SKILL.md의 접근성 수치와 동일 소스.
private val MinTouchTarget = 48.dp

/** 주 행동 버튼(Primary CTA). 항상 48dp 이상, 22sp 본문 크기를 따른다. */
@Composable
fun SeniorPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.defaultMinSize(minHeight = MinTouchTarget),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

/** 보조 행동 버튼(건너뛰기, 뒤로가기 등). */
@Composable
fun SeniorSecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = MinTouchTarget),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * 고를 수 있는 카드(테마·품목). 고른 상태는 굵은 초록 테두리 + 연초록 배경으로 표시해
 * 색 하나에만 기대지 않는다(테두리 두께도 달라짐). 카드 전체가 탭 영역이다.
 */
@Composable
fun SeniorCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) colors.primaryContainer else colors.surface,
        border = BorderStroke(if (selected) 3.dp else 1.5.dp, if (selected) colors.primary else colors.outline),
        shadowElevation = 1.dp,
        modifier = modifier
            .defaultMinSize(minHeight = MinTouchTarget)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            content = content,
        )
    }
}
