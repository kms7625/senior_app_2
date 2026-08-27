package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 웹 리서치 근거(Android Accessibility Help, Material Design 접근성 가이드) 기준
// 터치 타겟 최소 48dp — senior-game-dev/SKILL.md의 접근성 수치와 동일 소스.
private val MinTouchTarget = 48.dp

/** 주 행동 버튼(Primary CTA). 항상 48dp 이상, 22sp 본문 크기를 따른다. */
@Composable
fun SeniorPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
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
