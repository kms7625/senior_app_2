package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.data.ThemePool
import com.mose.seniorgame.state.GameSession

/** 와이어프레임 07: 설정 — 토글 사이 넉넉한 간격(탭 타겟 간 최소 32px 기준). */
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var autoDifficulty by remember { mutableStateOf(true) }
    var dualTask by remember { mutableStateOf(true) }
    var alwaysShowList by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ThemeRow()
        SettingRow("난이도 자동 조절 (AI)", autoDifficulty) { autoDifficulty = it }
        SettingRow("이중과제(계산 단계) 사용", dualTask) { dualTask = it }
        SettingRow("목록 다시 보기 항상 허용", alwaysShowList) { alwaysShowList = it }
        SeniorSecondaryButton(text = "뒤로가기", onClick = onBack)
    }
}

/**
 * 선호 테마. 탭할 때마다 [ThemePool.all] 순서대로 다음 테마로 순환한다 — 정식 목록
 * 선택 UI(드롭다운 등)는 이후 디자인 단계에서 다듬고, 지금은 탭 하나로 접근성을
 * 우선한다(복잡한 제스처 지양 원칙).
 */
@Composable
private fun ThemeRow() {
    val current = GameSession.currentTheme.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val next = ThemePool.all[(ThemePool.all.indexOf(current) + 1) % ThemePool.all.size]
                GameSession.selectTheme(next)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("선호 테마 (눌러서 변경)", style = MaterialTheme.typography.bodyLarge)
        Text(current.label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SettingRow(label: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = value, onCheckedChange = onChange)
    }
}
