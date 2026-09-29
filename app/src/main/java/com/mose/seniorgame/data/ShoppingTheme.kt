package com.mose.seniorgame.data

/**
 * 품목명, 매칭된 미끼(decoy), 계산대 단계용 임시 시세, 아이콘 리소스 슬러그.
 *
 * [iconSlug]·[decoyIconSlug]는 `app/src/main/res/drawable-nodpi/`의 `ic_item_<slug>.png`·
 * `ic_decoy_<slug>.png`와 1:1 대응한다. 미끼 slug는 이미지 패키지 매니페스트에서 목표 품목
 * 바로 오른쪽 칸(같은 테마·같은 줄)으로 짝지었다 — "장갑"·"가위"처럼 테마 간에 겹치는 한글
 * 이름이 있어 이름으로는 찾지 않는다.
 */
data class ShoppingItem(
    val name: String,
    val decoy: String,
    val price: Int,
    val iconSlug: String,
    val decoyIconSlug: String,
)

/**
 * docs/theme-item-pool.md의 테마 1개(목표 품목 10개 + 미끼 짝)에 대응.
 * [sections]는 동선 계획 화면(RouteScreen)에서 보여줄 매장 구역 3곳 — 계산대는
 * 모든 테마에 공통이라 여기 포함하지 않고 RouteScreen에서 고정으로 덧붙인다.
 */
data class ShoppingTheme(
    val id: String,
    val label: String,
    val items: List<ShoppingItem>,
    val sections: List<String>,
) {

    /** 계획 세우기 화면에 제시할 후보. MVP는 앞쪽 [count]개를 고정으로 쓴다. */
    fun candidates(count: Int = 4): List<Pair<String, String>> =
        items.take(count).map { it.name to it.decoy }

    fun itemOf(name: String): ShoppingItem? = items.firstOrNull { it.name == name }

    fun decoyOf(target: String): String? = itemOf(target)?.decoy

    /** 미끼 이름으로 미끼 그림 slug를 찾는다. 테마 안에서는 미끼 이름이 겹치지 않는다. */
    fun decoyIconOf(decoy: String): String? = items.firstOrNull { it.decoy == decoy }?.decoyIconSlug

    fun priceOf(target: String): Int = itemOf(target)?.price ?: 0

    fun totalPrice(selected: List<String>): Int = selected.sumOf { priceOf(it) }
}

/**
 * docs/theme-item-pool.md 6개 테마를 그대로 옮긴 것. 성별·경험 편향 최소화, 보편
 * 생활용품 우선 원칙에 따라 선정된 품목들이다(같은 문서 "선정 기준" 참고).
 */
object ThemePool {
    val cooking = ShoppingTheme(
        id = "cooking",
        label = "요리 재료",
        items = listOf(
            ShoppingItem("두부", "순두부", 1500, "tofu", "silken_tofu"),
            ShoppingItem("계란", "메추리알", 4000, "egg", "quail_eggs"),
            ShoppingItem("대파", "쪽파", 2000, "green_onion", "chives"),
            ShoppingItem("마늘", "양파", 2500, "garlic", "onion"),
            ShoppingItem("라면", "국수", 4500, "ramen", "noodles"),
            ShoppingItem("식용유", "참기름", 6000, "cooking_oil", "sesame_oil"),
            ShoppingItem("설탕", "소금", 2000, "sugar", "salt"),
            ShoppingItem("우유", "두유", 2800, "milk", "soy_milk"),
            ShoppingItem("김", "미역", 3000, "seaweed", "kelp"),
            ShoppingItem("고추장", "된장", 5000, "gochujang", "doenjang"),
        ),
        sections = listOf("채소 코너", "정육 코너", "유제품 코너"),
    )

    val tools = ShoppingTheme(
        id = "tools",
        label = "공구·생활수리",
        items = listOf(
            ShoppingItem("건전지", "충전지", 3000, "battery", "rechargeable_battery"),
            ShoppingItem("못", "나사", 1500, "nail", "screw"),
            ShoppingItem("전구", "LED등", 4000, "bulb", "led_bulb"),
            ShoppingItem("테이프", "접착제", 2000, "tape", "adhesive"),
            ShoppingItem("드라이버", "렌치", 5000, "screwdriver", "wrench"),
            ShoppingItem("장갑", "목장갑", 3000, "gloves_tools", "cotton_work_gloves"),
            ShoppingItem("손전등", "랜턴", 8000, "flashlight", "lantern"),
            ShoppingItem("자물쇠", "열쇠고리", 6000, "lock", "key_ring"),
            ShoppingItem("우산", "양산", 9000, "umbrella", "sun_parasol"),
            ShoppingItem("빗자루", "밀대", 7000, "broom", "flat_mop"),
        ),
        sections = listOf("공구 코너", "전기용품 코너", "생활잡화 코너"),
    )

    val garden = ShoppingTheme(
        id = "garden",
        label = "원예·화초",
        items = listOf(
            ShoppingItem("화분", "화분받침", 4000, "pot", "pot_saucer"),
            ShoppingItem("씨앗", "모종", 2000, "seed", "seedlings"),
            ShoppingItem("물뿌리개", "분무기", 5000, "watering_can", "spray_mister"),
            ShoppingItem("흙", "비료", 3000, "soil", "fertilizer"),
            ShoppingItem("장갑", "앞치마", 3000, "gloves_garden", "apron"),
            ShoppingItem("가위", "전지가위", 6000, "scissors_garden", "pruning_shears"),
            ShoppingItem("삽", "호미", 7000, "shovel", "hoe"),
            ShoppingItem("화분흙", "마사토", 4000, "potting_soil", "akadama_soil"),
            ShoppingItem("지지대", "끈", 2000, "stake", "garden_twine"),
            ShoppingItem("화초", "다육이", 8000, "plant", "succulent"),
        ),
        sections = listOf("화분 코너", "씨앗·모종 코너", "원예용품 코너"),
    )

    val pharmacy = ShoppingTheme(
        id = "pharmacy",
        label = "약국·생필품",
        items = listOf(
            ShoppingItem("휴지", "물티슈", 3000, "tissue", "wet_wipes"),
            ShoppingItem("비누", "샴푸", 2500, "soap", "shampoo"),
            ShoppingItem("칫솔", "치약", 2000, "toothbrush", "toothpaste"),
            ShoppingItem("반창고", "거즈", 1500, "bandage", "gauze"),
            ShoppingItem("마스크", "소독약", 3000, "mask", "antiseptic"),
            ShoppingItem("세제", "섬유유연제", 5000, "detergent", "fabric_softener"),
            ShoppingItem("수건", "손수건", 4000, "towel", "handkerchief"),
            ShoppingItem("로션", "크림", 6000, "lotion", "cream"),
            ShoppingItem("밴드", "파스", 3000, "band_aid", "pain_patch"),
            ShoppingItem("면봉", "솜", 1500, "cotton_swab", "cotton_ball"),
        ),
        sections = listOf("위생용품 코너", "세제 코너", "구급용품 코너"),
    )

    val stationery = ShoppingTheme(
        id = "stationery",
        label = "문구·우편",
        items = listOf(
            ShoppingItem("편지지", "엽서", 1500, "letter_paper", "postcard"),
            ShoppingItem("봉투", "상자", 2000, "envelope", "cardboard_box"),
            ShoppingItem("우표", "스티커", 500, "stamp", "decorative_sticker"),
            ShoppingItem("볼펜", "연필", 1000, "pen", "pencil"),
            ShoppingItem("풀", "테이프", 1500, "glue", "tape"),
            ShoppingItem("가위", "커터칼", 3000, "scissors_stationery", "box_cutter"),
            ShoppingItem("달력", "수첩", 4000, "calendar", "notebook"),
            ShoppingItem("도장", "인주", 5000, "seal_stamp", "ink_pad"),
            ShoppingItem("노끈", "고무줄", 1000, "string", "rubber_bands"),
            ShoppingItem("메모지", "포스트잇", 1500, "memo_pad", "sticky_notes"),
        ),
        sections = listOf("문구 코너", "우편용품 코너", "사무용품 코너"),
    )

    val pet = ShoppingTheme(
        id = "pet",
        label = "반려동물 용품",
        items = listOf(
            ShoppingItem("사료", "간식", 8000, "pet_food", "pet_treats"),
            ShoppingItem("물그릇", "밥그릇", 5000, "water_bowl", "food_bowl"),
            ShoppingItem("배변패드", "모래", 6000, "pee_pad", "cat_litter"),
            ShoppingItem("목줄", "하네스", 9000, "leash", "harness"),
            ShoppingItem("빗", "발톱깎이", 4000, "pet_comb", "nail_clipper"),
            ShoppingItem("방석", "담요", 7000, "cushion", "blanket"),
            ShoppingItem("장난감", "공", 3000, "toy", "ball"),
            ShoppingItem("샴푸", "린스", 6000, "pet_shampoo", "conditioner"),
            ShoppingItem("이동가방", "켄넬", 15000, "carrier", "kennel"),
            ShoppingItem("물티슈", "휴지", 3000, "wet_wipes", "toilet_tissue"),
        ),
        sections = listOf("사료 코너", "용품 코너", "미용용품 코너"),
    )

    val all: List<ShoppingTheme> = listOf(cooking, tools, garden, pharmacy, stationery, pet)

    fun byId(id: String): ShoppingTheme = all.firstOrNull { it.id == id } ?: cooking
}
