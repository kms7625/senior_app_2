package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.state.GameSession

/** 와이어프레임 01: 홈 / 오늘의 손님맞이 시작. */
@Composable
fun HomeScreen(onStart: () -> Unit, onSettings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        Text("${GameSession.roundNumber.value}일째 손님맞이", style = MaterialTheme.typography.bodyLarge)
        Text("오늘은 누구를 우리 집에 초대해 볼까요?", style = MaterialTheme.typography.titleLarge)
        SeniorPrimaryButton(text = "오늘의 손님맞이 시작하기", onClick = onStart)
        SeniorSecondaryButton(text = "설정", onClick = onSettings)
    }
}
