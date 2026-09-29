package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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

/**
 * 와이어프레임 07: 설정 — 토글 사이 넉넉한 간격(탭 타겟 간 최소 32px 기준).
 * 토글은 [GameSession]에 직접 연결돼 실제로 게임 동작을 바꾼다(코드 리뷰에서
 * 로컬 remember 상태뿐인 죽은 UI로 지적된 부분, 2026-08-27 연결).
 */
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ThemeRow()
        SettingRow("난이도 자동 조절 (AI)", GameSession.autoDifficultyEnabled.value, GameSession::setAutoDifficulty)
        SettingRow("이중과제(계산 단계) 사용", GameSession.dualTaskEnabled.value, GameSession::setDualTask)
        SettingRow("목록 다시 보기 항상 허용", GameSession.alwaysShowListEnabled.value, GameSession::setAlwaysShowList)
        SeniorSecondaryButton(text = "뒤로가기", onClick = onBack)
    }
}

/**
 * 선호 테마. 현재 테마 이름을 누르면 바로 아래에 작은 드롭다운 목록이 겹쳐 뜬다 —
 * 아래 설정 행을 밀어내지 않는다. 각 항목은 DropdownMenuItem 기본 높이(48dp)라
 * 터치 타겟 기준을 지킨다. 현재 테마에는 ✓ 표시.
 */
@Composable
private fun ThemeRow() {
    val current = GameSession.currentTheme.value
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("선호 테마", style = MaterialTheme.typography.bodyLarge)
        Box {
            Text(
                text = "${current.label}  ▼",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(vertical = 12.dp),
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ThemePool.all.forEach { theme ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (theme.id == current.id) "✓ ${theme.label}" else theme.label,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        onClick = {
                            if (theme.id != current.id) GameSession.selectTheme(theme)
                            expanded = false
                        },
                    )
                }
            }
        }
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
