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
 * 와이어프레임 06: 결과 화면.
 * 실패 라운드여도 동일한 긍정 톤 유지, 감점 숫자·경고색 없음(docs/GDD.md ⑤ 참고).
 * 상차림 일러스트는 `res/drawable-nodpi/ic_scene_result_table.png`(생성 이미지) — 다른
 * 그림으로 바꿀 때는 같은 이름의 PNG만 교체하면 된다.
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
            modifier = Modifier.size(240.dp),
        )
        Text("\"오늘도 손님이 참 좋아했어요\"", style = MaterialTheme.typography.titleLarge)
        if (GameSession.collectedItems.isNotEmpty()) {
            Text(
                "오늘 준비한 것: ${GameSession.collectedItems.joinToString(", ")}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        // 다음 라운드 방식은 온디바이스 AI(TFLite)가 방금 라운드 성적으로 정한 값이다 —
        // 난이도 변화를 미리 알려 갑자기 어려워졌다는 느낌을 줄인다.
        Text(
            if (GameSession.showListHint) "다음에도 목록을 보면서 찾아볼 거예요" else "다음엔 목록 없이 떠올려서 찾아볼 거예요",
            style = MaterialTheme.typography.bodyLarge,
        )
        SeniorPrimaryButton(text = "홈으로 돌아가기", onClick = onHome)
    }
}
