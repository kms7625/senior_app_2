package com.mose.seniorgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mose.seniorgame.ui.screens.CheckoutScreen
import com.mose.seniorgame.ui.screens.HomeScreen
import com.mose.seniorgame.ui.screens.OnboardingScreen
import com.mose.seniorgame.ui.screens.PlanScreen
import com.mose.seniorgame.ui.screens.ResultScreen
import com.mose.seniorgame.ui.screens.RoundFrame
import com.mose.seniorgame.ui.screens.RouteScreen
import com.mose.seniorgame.ui.screens.SearchScreen
import com.mose.seniorgame.ui.screens.SettingsScreen
import com.mose.seniorgame.ui.theme.SeniorAppTheme
import com.mose.seniorgame.state.GameSession
import com.mose.seniorgame.ai.DifficultyModel

// 화면 순서는 docs/GDD.md "사용자 진행 흐름"과 동일:
// 홈 → 계획 세우기 → 동선 계획 → 매장 탐색 → (선택) 계산대 → 결과 → 홈
object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PLAN = "plan"
    const val ROUTE = "route"
    const val SEARCH = "search"
    const val CHECKOUT = "checkout"
    const val RESULT = "result"
    const val SETTINGS = "settings"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 온디바이스 AI 난이도 모델 초기화 — 완전 오프라인, assets 번들 파일만 읽는다.
        DifficultyModel.initialize(applicationContext)
        // 온보딩 여부·선호 테마 복원 — setContent보다 먼저 호출해야 시작 화면이 맞다.
        GameSession.initializePersistence(applicationContext)
        setContent {
            SeniorAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
private fun AppNavHost() {
    val navController = rememberNavController()
    val startDestination = if (GameSession.hasOnboarded.value) Routes.HOME else Routes.ONBOARDING
    // 진행 중 "나가기" — 라운드는 넘기지 않고 홈으로만 돌아간다(RoundFrame 참고).
    val exitToHome: () -> Unit = { navController.popBackStack(Routes.HOME, inclusive = false) }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onThemeChosen = { theme ->
                    GameSession.selectTheme(theme)
                    GameSession.completeOnboarding()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onStart = { navController.navigate(Routes.PLAN) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.PLAN) {
            RoundFrame(onExit = exitToHome) { PlanScreen(onConfirm = { navController.navigate(Routes.ROUTE) }) }
        }
        composable(Routes.ROUTE) {
            RoundFrame(onExit = exitToHome) { RouteScreen(onConfirm = { navController.navigate(Routes.SEARCH) }) }
        }
        composable(Routes.SEARCH) {
            RoundFrame(onExit = exitToHome) {
                SearchScreen(
                    onDone = {
                        // "이중과제 사용" 토글이 꺼져 있으면 계산대를 건너뛰고 바로 결과로.
                        val next = if (GameSession.dualTaskEnabled.value) Routes.CHECKOUT else Routes.RESULT
                        navController.navigate(next)
                    },
                )
            }
        }
        composable(Routes.CHECKOUT) {
            RoundFrame(onExit = exitToHome) {
                CheckoutScreen(
                    onConfirm = { navController.navigate(Routes.RESULT) },
                    onSkip = { navController.navigate(Routes.RESULT) },
                )
            }
        }
        composable(Routes.RESULT) {
            ResultScreen(
                onHome = {
                    GameSession.advanceRound()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
