package com.mose.seniorgame.ui.screens

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
        SettingRow("난이도 자동 조절 (AI)", autoDifficulty) { autoDifficulty = it }
        SettingRow("이중과제(계산 단계) 사용", dualTask) { dualTask = it }
        SettingRow("목록 다시 보기 항상 허용", alwaysShowList) { alwaysShowList = it }
        SeniorSecondaryButton(text = "뒤로가기", onClick = onBack)
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
