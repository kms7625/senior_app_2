package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.data.ShoppingTheme
import com.mose.seniorgame.data.ThemePool

/**
 * 첫 실행 온보딩 — 선호 테마를 먼저 고르게 한다(GDD "개인화" 설계 원칙).
 * "무엇을 준비하는 게 더 익숙하세요?"처럼 플레이어가 스스로 고르는 문구를 쓰고,
 * "심부름" 같은 수동적 표현은 쓰지 않는다("우리 집에 놀러와" 서사의 주체성 원칙,
 * docs/GDD.md 참고).
 *
 * [GameSession.initializePersistence]가 DataStore로 저장하므로 앱을 껐다 켜도 다시
 * 뜨지 않는다. 설정 화면의 "선호 테마" 목록에서 언제든 다시 바꿀 수 있다.
 */
@Composable
fun OnboardingScreen(onThemeChosen: (ShoppingTheme) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("무엇을 준비하는 게 더 익숙하세요?", style = MaterialTheme.typography.titleLarge)
        Text(
            "처음엔 익숙한 주제로 시작해요. 나중에 설정에서 언제든 바꿀 수 있어요.",
            style = MaterialTheme.typography.bodyLarge,
        )
        // 테마 6개를 3개씩 두 줄의 카드로 — 테마 아이콘(ic_theme_<id>)과 이름을 함께 보여준다.
        ThemePool.all.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { theme ->
                    SeniorCard(onClick = { onThemeChosen(theme) }, modifier = Modifier.weight(1f)) {
                        ItemIcon(drawableName = "ic_theme_${theme.id}", modifier = Modifier.size(72.dp))
                        Text(theme.label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
