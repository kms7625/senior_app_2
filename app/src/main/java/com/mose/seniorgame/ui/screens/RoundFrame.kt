package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 라운드 진행 화면(계획·동선·찾기·계산대) 공통 틀 — 오른쪽 위에 "나가기" 버튼을 둔다.
 * 실수로 누르는 경우를 대비해 한 번 확인한 뒤 [onExit]를 부른다. 고른 물건 등 진행
 * 상태는 그대로 저장돼 있어 다음에 이어서 할 수 있다(라운드를 넘기지 않음).
 */
@Composable
fun RoundFrame(onExit: () -> Unit, content: @Composable () -> Unit) {
    var confirming by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 24.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            SeniorSecondaryButton(text = "나가기", onClick = { confirming = true })
        }
        Box(Modifier.weight(1f)) { content() }
    }
    if (confirming) {
        AlertDialog(
            onDismissRequest = { confirming = false },
            title = { Text("처음 화면으로 갈까요?", style = MaterialTheme.typography.titleLarge) },
            text = { Text("고른 물건은 그대로 남아 있어서 다음에 이어서 할 수 있어요.", style = MaterialTheme.typography.bodyLarge) },
            confirmButton = {
                SeniorPrimaryButton(text = "처음 화면으로", onClick = { confirming = false; onExit() })
            },
            dismissButton = {
                SeniorSecondaryButton(text = "계속하기", onClick = { confirming = false })
            },
        )
    }
}
