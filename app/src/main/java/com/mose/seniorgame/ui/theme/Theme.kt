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

// 품목 그림(생성 이미지)의 아이보리 배경과 초록 소품 톤에 맞춘 팔레트(2026-09-30).
// 대비: Ink/Paper 약 13:1, 흰 글자/Leaf 버튼 약 6.3:1 — 둘 다 WCAG AA(4.5:1) 이상.
private val Paper = Color(0xFFFAF4E6)
private val Card = Color(0xFFFFFCF5)
private val Ink = Color(0xFF2B2A24)
private val Leaf = Color(0xFF2F6B4F)
private val LeafSoft = Color(0xFFDDEBDF)
private val LeafDeep = Color(0xFF1E4A35)
private val Line = Color(0xFFCFC3AE)

private val DarkPaper = Color(0xFF1B1F1B)
private val DarkCard = Color(0xFF242A25)
private val DarkInk = Color(0xFFEDEAE0)
private val DarkLeaf = Color(0xFF8CC7A5)
private val DarkLeafSoft = Color(0xFF2E4A3A)
private val DarkLine = Color(0xFF4A5249)

private val LightColors = lightColorScheme(
    background = Paper,
    surface = Card,
    primary = Leaf,
    onPrimary = Color.White,
    primaryContainer = LeafSoft,
    onPrimaryContainer = LeafDeep,
    onBackground = Ink,
    onSurface = Ink,
    outline = Line,
)

private val DarkColors = darkColorScheme(
    background = DarkPaper,
    surface = DarkCard,
    primary = DarkLeaf,
    onPrimary = Color(0xFF0F2A1D),
    primaryContainer = DarkLeafSoft,
    onPrimaryContainer = DarkInk,
    onBackground = DarkInk,
    onSurface = DarkInk,
    outline = DarkLine,
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
