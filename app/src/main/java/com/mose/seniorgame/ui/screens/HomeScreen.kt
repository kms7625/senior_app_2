package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.R
import com.mose.seniorgame.state.GameSession

/**
 * 와이어프레임 01: 홈 / 오늘의 손님맞이 시작.
 * 캐릭터 일러스트는 아직 플레이스홀더(`ic_scene_home_character.xml`) — 실제 그림이
 * 나오면 이 파일만 교체하면 된다(ShoppingTheme.kt의 iconSlug 방식과 동일 원칙).
 */
@Composable
fun HomeScreen(onStart: () -> Unit, onSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_scene_home_character),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
        )
        Text("${GameSession.roundNumber.value}일째 손님맞이", style = MaterialTheme.typography.bodyLarge)
        Text("오늘은 누구를 우리 집에 초대해 볼까요?", style = MaterialTheme.typography.titleLarge)
        SeniorPrimaryButton(text = "오늘의 손님맞이 시작하기", onClick = onStart)
        SeniorSecondaryButton(text = "설정", onClick = onSettings)
    }
}
