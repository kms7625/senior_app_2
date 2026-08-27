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
 * 품목 아이콘 표시. `iconSlug`로 `res/drawable/ic_item_<slug>.xml`을 찾아 보여준다.
 * 지금은 전부 같은 플레이스홀더를 가리키지만(ShoppingTheme.kt 문서 참고), 실제
 * 일러스트가 준비되면 해당 slug 파일만 교체하면 되고 이 컴포저블은 그대로 쓴다.
 * 리소스를 못 찾으면(파일이 아직 없거나 삭제됐으면) 자동으로 플레이스홀더로 대체한다.
 */
@Composable
fun ItemIcon(iconSlug: String, modifier: Modifier = Modifier.size(28.dp)) {
    val context = LocalContext.current
    val resId = remember(iconSlug) {
        val id = context.resources.getIdentifier("ic_item_$iconSlug", "drawable", context.packageName)
        if (id != 0) id else R.drawable.ic_item_placeholder
    }
    Image(painter = painterResource(id = resId), contentDescription = null, modifier = modifier)
}
