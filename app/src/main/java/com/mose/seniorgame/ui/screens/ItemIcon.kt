package com.mose.seniorgame.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mose.seniorgame.R

/**
 * 품목 그림 표시. [drawableName](예: `ic_item_tofu`, `ic_decoy_silken_tofu`)으로
 * `res/drawable-nodpi/`의 PNG를 찾아 보여준다. 리소스를 못 찾으면 자동으로
 * 플레이스홀더(`ic_item_placeholder.xml`)로 대체한다.
 */
@Composable
fun ItemIcon(drawableName: String, modifier: Modifier = Modifier.size(48.dp)) {
    val context = LocalContext.current
    val resId = remember(drawableName) {
        val id = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        if (id != 0) id else R.drawable.ic_item_placeholder
    }
    Image(painter = painterResource(id = resId), contentDescription = null, modifier = modifier)
}
