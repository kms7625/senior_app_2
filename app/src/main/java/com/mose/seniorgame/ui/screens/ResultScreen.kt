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

/**
 * 와이어프레임 06: 결과 화면.
 * 실패 라운드여도 동일한 긍정 톤 유지, 감점 숫자·경고색 없음(docs/GDD.md ⑤ 참고).
 * 상차림 일러스트는 아직 플레이스홀더(`ic_scene_result_table.xml`) — 실제 그림이
 * 나오면 이 파일만 교체하면 된다.
 */
@Composable
fun ResultScreen(onHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_scene_result_table),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
        )
        Text("\"오늘도 손님이 참 좋아했어요\"", style = MaterialTheme.typography.titleLarge)
        SeniorPrimaryButton(text = "홈으로 돌아가기", onClick = onHome)
    }
}
