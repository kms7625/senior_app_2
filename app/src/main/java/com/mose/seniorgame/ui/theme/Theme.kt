package com.mose.seniorgame.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// docs/wireframes.html의 CSS 토큰과 동일한 팔레트.
private val Paper = Color(0xFFF4F6F3)
private val Surface = Color(0xFFFFFFFF)
private val Ink = Color(0xFF223027)
private val Accent = Color(0xFFB8791B)

private val DarkPaper = Color(0xFF171D19)
private val DarkSurface = Color(0xFF1E2620)
private val DarkInk = Color(0xFFE7EDE6)
private val DarkAccent = Color(0xFFE0A94A)

private val LightColors = lightColorScheme(
    background = Paper,
    surface = Surface,
    primary = Accent,
    onBackground = Ink,
    onSurface = Ink,
)

private val DarkColors = darkColorScheme(
    background = DarkPaper,
    surface = DarkSurface,
    primary = DarkAccent,
    onBackground = DarkInk,
    onSurface = DarkInk,
)

// 학생안내 "글자 크기 충분히 크게" 원칙 + 웹 리서치 근거(PMC7330495, 22pt에서
// 고령자 그룹이 청년 그룹과 동등한 수행) 기준으로 본문 22sp를 기본값으로 잡는다.
private val SeniorTypography = Typography(
    bodyLarge = TextStyle(fontSize = 22.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontSize = 26.sp, lineHeight = 32.sp),
    labelLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp),
)

@Composable
fun SeniorAppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = SeniorTypography,
        content = content,
    )
}
