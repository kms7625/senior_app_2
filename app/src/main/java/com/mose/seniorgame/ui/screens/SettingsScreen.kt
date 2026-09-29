package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
            .verticalScroll(rememberScrollState())
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
 * 선호 테마. 행을 누르면 6개 테마 목록이 그 자리에 펼쳐지고, 하나를 고르면 적용 후
 * 다시 접힌다. 드롭다운 대신 큰 행 목록 — 작은 메뉴·스크롤 제스처를 피한다
 * (senior-game-dev 접근성 기준). 현재 테마에는 ✓ 표시.
 */
@Composable
private fun ThemeRow() {
    val current = GameSession.currentTheme.value
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("선호 테마", style = MaterialTheme.typography.bodyLarge)
            Text(if (expanded) "${current.label}  ▲" else "${current.label}  ▼", style = MaterialTheme.typography.bodyLarge)
        }
        if (expanded) {
            ThemePool.all.forEach { theme ->
                Text(
                    text = if (theme.id == current.id) "✓ ${theme.label}" else theme.label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (theme.id != current.id) GameSession.selectTheme(theme)
                            expanded = false
                        }
                        .padding(start = 24.dp, top = 14.dp, bottom = 14.dp),
                )
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
