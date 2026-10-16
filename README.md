# 우리 집에 놀러와 — 시니어 장보기 기억 게임 (Android)

손님을 맞이할 물건을 기억해 장을 보는 흐름을 따라가며, 목록 기억·매장 동선·계산을 연습하는 시니어 대상 Android 게임입니다.
라운드 성과를 보고 다음 라운드 난이도(목록 힌트 노출 여부)를 **기기 안의 TFLite 모델**이 정합니다.

## 게임 흐름

온보딩(선호 테마 선택) → **계획**(살 물건 고르기) → **동선**(매장 지도에서 필요한 코너만 들르기) → **찾기**(비슷한 미끼 물건 사이에서 목록의 물건 찾기) → **계산대**(합계를 보기 3개 중에서 고르는 암산) → **결과**(준비한 물건 요약·다음 라운드 방식 안내)

- 테마 6종: 요리 재료 · 공구·생활수리 · 원예·화초 · 약국·생필품 · 문구·우편 · 반려동물 용품
- 품목 60개마다 매장 코너와 미끼 물건(비슷하지만 목록에 없는 물건) 그림 지정
- 진행 중 나가기·이전 단계로 돌아가기, 라운드 진행 상태까지 DataStore에 저장해 앱이 종료돼도 이어하기

## 온디바이스 AI — 난이도 조절

`ai/DifficultyModel.kt`가 `assets/difficulty_model.tflite`를 메모리 매핑으로 읽어 추론합니다. 네트워크 호출은 없습니다.

| 입력 | 정규화 |
|---|---|
| 직전 라운드 정답률 | 0~1 |
| 반응 시간 | 초 / 10, 최대 1 |
| 라운드 번호 | 라운드 / 10, 최대 1 |

출력이 0.5보다 크면 다음 라운드에서 목록 힌트를 숨깁니다. 첫 라운드와 모델 초기화 전에는 힌트를 보여주는 기본값으로 시작합니다.

## 구조

```
app/src/main/java/com/mose/seniorgame/
├── ai/DifficultyModel.kt      TFLite 난이도 추론
├── data/                      테마·품목(ShoppingTheme), 동선(RoutePlan), 계산 문제(CheckoutQuiz)
├── state/GameSession.kt       화면 간 공유 상태 + DataStore 영구 저장
└── ui/screens/                온보딩·홈·계획·동선·찾기·계산대·결과·설정
```

## 테스트

- 단위 테스트 7개 파일 · 39개 케이스(`app/src/test`) — 게임 상태, 동선, 계산 문제, 테마, 품목 그림 존재 여부, 조사(助詞) 처리, 난이도 모델
- 계측 테스트 `AppFlowTest` 4개 케이스(`app/src/androidTest`)

```bash
./gradlew testDebugUnitTest          # 단위 테스트
./gradlew connectedDebugAndroidTest  # 기기·에뮬레이터 필요
```

## 기술 스택

Kotlin · Jetpack Compose · Navigation · DataStore Preferences · TensorFlow Lite — minSdk 26 / targetSdk 34
