package com.mose.seniorgame

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mose.seniorgame.state.GameSession
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * 실제 기기/에뮬레이터에서 도는 계측 테스트. 2026-08-27에 에뮬레이터로 수동
 * 확인했던 전체 플로우(홈→계획→동선→탐색→계산대→결과→홈)를 자동화한 것이다.
 * MainActivity가 실제로 실행되므로 DifficultyModel.initialize()도 실제 assets에서
 * 로드되고, 이 테스트가 통과하면 TFLite 모델 로드·추론이 실기에서 크래시 없이
 * 동작한다는 것까지 함께 검증된다.
 */
class AppFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        GameSession.resetForTest()
    }

    @After
    fun tearDown() {
        GameSession.resetForTest()
    }

    @Test
    fun fullRoundFlow_fromHomeBackToHome_withoutCrashing() {
        // 온보딩 — GameSession.resetForTest()는 hasOnboarded=false이므로 앱 시작 시
        // 항상 이 화면부터 나온다. 첫 테마(요리 재료)를 골라 넘어간다.
        composeTestRule.onNodeWithText("요리 재료").performClick()

        // 홈 — 시작 문구가 보이는지
        composeTestRule.onNodeWithText("오늘은 누구를 우리 집에 초대해 볼까요?").assertExists()
        composeTestRule.onNodeWithText("오늘의 손님맞이 시작하기").performClick()

        // 계획 세우기 — 요리 재료 테마 기본 후보 중 2개 선택
        composeTestRule.onNodeWithText("두부").performClick()
        composeTestRule.onNodeWithText("계란").performClick()
        composeTestRule.onNode(hasText("목록 확정하기", substring = true)).performClick()

        // 동선 계획 — 4개 코너를 순서대로
        composeTestRule.onNodeWithText("채소 코너").performClick()
        composeTestRule.onNodeWithText("정육 코너").performClick()
        composeTestRule.onNodeWithText("유제품 코너").performClick()
        composeTestRule.onNodeWithText("계산대").performClick()
        composeTestRule.onNodeWithText("가장 덜 걷는 길을 찾으셨어요!").assertExists()
        composeTestRule.onNode(hasText("동선 확정하기", substring = true)).performClick()

        // 매장 탐색 — GameSession.resetForTest() 직후라 showListHint=true(1라운드
        // 기본값)이므로 목표 품목 라벨에는 " ·"가 붙는다. "두부"만으로 substring
        // 매칭하면 미끼 "순두부"도 걸려서 모호해지므로 정확한 라벨로 찾는다.
        composeTestRule.onNodeWithText("순두부").performClick()
        composeTestRule.onNodeWithText("순두부는 비슷하지만 목록에 없는 물건이에요").assertExists()
        composeTestRule.onNodeWithText("두부 ·").performClick()
        composeTestRule.onNodeWithText("계란 ·").performClick()
        composeTestRule.onNode(hasText("다 담았어요", substring = true)).performClick()

        // 계산대 — 보기 3개 중 정답(두부 1500 + 계란 4000 = 5500)을 골라야 확인 버튼이 나온다
        composeTestRule.onNodeWithText("확인").assertDoesNotExist()
        composeTestRule.onNodeWithText("5500원").performClick()
        composeTestRule.onNode(hasText("맞아요, 5500원", substring = true)).assertExists()
        composeTestRule.onNodeWithText("확인").performClick()

        // 결과 — 홈으로 복귀하면 라운드가 2일째로 늘어야 한다
        composeTestRule.onNode(hasText("오늘 준비한 것:", substring = true)).assertExists()
        composeTestRule.onNodeWithText("홈으로 돌아가기").performClick()
        composeTestRule.onNodeWithText("2일째 손님맞이").assertExists()
    }
}
