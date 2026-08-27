package com.mose.seniorgame.data

/**
 * 품목명, 매칭된 미끼(decoy), 계산대 단계용 임시 시세, 아이콘 리소스 슬러그.
 *
 * [iconSlug]는 `app/src/main/res/drawable/ic_item_<slug>.xml` 파일명과 1:1 대응한다.
 * 지금은 전부 같은 플레이스홀더 그림을 가리키지만, 실제 일러스트가 준비되면 코드는
 * 하나도 안 건드리고 해당 slug의 XML 파일만 교체하면 된다(`docs/assets/icons/README.md`
 * "미완성 범위" 참고).
 */
data class ShoppingItem(val name: String, val decoy: String, val price: Int, val iconSlug: String)

/** docs/theme-item-pool.md의 테마 1개(목표 품목 10개 + 미끼 짝)에 대응. */
data class ShoppingTheme(val id: String, val label: String, val items: List<ShoppingItem>) {

    /** 계획 세우기 화면에 제시할 후보. MVP는 앞쪽 [count]개를 고정으로 쓴다. */
    fun candidates(count: Int = 4): List<Pair<String, String>> =
        items.take(count).map { it.name to it.decoy }

    fun itemOf(name: String): ShoppingItem? = items.firstOrNull { it.name == name }

    fun decoyOf(target: String): String? = itemOf(target)?.decoy

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
            ShoppingItem("두부", "순두부", 1500, "tofu"),
            ShoppingItem("계란", "메추리알", 4000, "egg"),
            ShoppingItem("대파", "쪽파", 2000, "green_onion"),
            ShoppingItem("마늘", "양파", 2500, "garlic"),
            ShoppingItem("라면", "국수", 4500, "ramen"),
            ShoppingItem("식용유", "참기름", 6000, "cooking_oil"),
            ShoppingItem("설탕", "소금", 2000, "sugar"),
            ShoppingItem("우유", "두유", 2800, "milk"),
            ShoppingItem("김", "미역", 3000, "seaweed"),
            ShoppingItem("고추장", "된장", 5000, "gochujang"),
        ),
    )

    val tools = ShoppingTheme(
        id = "tools",
        label = "공구·생활수리",
        items = listOf(
            ShoppingItem("건전지", "충전지", 3000, "battery"),
            ShoppingItem("못", "나사", 1500, "nail"),
            ShoppingItem("전구", "LED등", 4000, "bulb"),
            ShoppingItem("테이프", "접착제", 2000, "tape"),
            ShoppingItem("드라이버", "렌치", 5000, "screwdriver"),
            ShoppingItem("장갑", "목장갑", 3000, "gloves_tools"),
            ShoppingItem("손전등", "랜턴", 8000, "flashlight"),
            ShoppingItem("자물쇠", "열쇠고리", 6000, "lock"),
            ShoppingItem("우산", "양산", 9000, "umbrella"),
            ShoppingItem("빗자루", "밀대", 7000, "broom"),
        ),
    )

    val garden = ShoppingTheme(
        id = "garden",
        label = "원예·화초",
        items = listOf(
            ShoppingItem("화분", "화분받침", 4000, "pot"),
            ShoppingItem("씨앗", "모종", 2000, "seed"),
            ShoppingItem("물뿌리개", "분무기", 5000, "watering_can"),
            ShoppingItem("흙", "비료", 3000, "soil"),
            ShoppingItem("장갑", "앞치마", 3000, "gloves_garden"),
            ShoppingItem("가위", "전지가위", 6000, "scissors_garden"),
            ShoppingItem("삽", "호미", 7000, "shovel"),
            ShoppingItem("화분흙", "마사토", 4000, "potting_soil"),
            ShoppingItem("지지대", "끈", 2000, "stake"),
            ShoppingItem("화초", "다육이", 8000, "plant"),
        ),
    )

    val pharmacy = ShoppingTheme(
        id = "pharmacy",
        label = "약국·생필품",
        items = listOf(
            ShoppingItem("휴지", "물티슈", 3000, "tissue"),
            ShoppingItem("비누", "샴푸", 2500, "soap"),
            ShoppingItem("칫솔", "치약", 2000, "toothbrush"),
            ShoppingItem("반창고", "거즈", 1500, "bandage"),
            ShoppingItem("마스크", "소독약", 3000, "mask"),
            ShoppingItem("세제", "섬유유연제", 5000, "detergent"),
            ShoppingItem("수건", "손수건", 4000, "towel"),
            ShoppingItem("로션", "크림", 6000, "lotion"),
            ShoppingItem("밴드", "파스", 3000, "band_aid"),
            ShoppingItem("면봉", "솜", 1500, "cotton_swab"),
        ),
    )

    val stationery = ShoppingTheme(
        id = "stationery",
        label = "문구·우편",
        items = listOf(
            ShoppingItem("편지지", "엽서", 1500, "letter_paper"),
            ShoppingItem("봉투", "상자", 2000, "envelope"),
            ShoppingItem("우표", "스티커", 500, "stamp"),
            ShoppingItem("볼펜", "연필", 1000, "pen"),
            ShoppingItem("풀", "테이프", 1500, "glue"),
            ShoppingItem("가위", "커터칼", 3000, "scissors_stationery"),
            ShoppingItem("달력", "수첩", 4000, "calendar"),
            ShoppingItem("도장", "인주", 5000, "seal_stamp"),
            ShoppingItem("노끈", "고무줄", 1000, "string"),
            ShoppingItem("메모지", "포스트잇", 1500, "memo_pad"),
        ),
    )

    val pet = ShoppingTheme(
        id = "pet",
        label = "반려동물 용품",
        items = listOf(
            ShoppingItem("사료", "간식", 8000, "pet_food"),
            ShoppingItem("물그릇", "밥그릇", 5000, "water_bowl"),
            ShoppingItem("배변패드", "모래", 6000, "pee_pad"),
            ShoppingItem("목줄", "하네스", 9000, "leash"),
            ShoppingItem("빗", "발톱깎이", 4000, "pet_comb"),
            ShoppingItem("방석", "담요", 7000, "cushion"),
            ShoppingItem("장난감", "공", 3000, "toy"),
            ShoppingItem("샴푸", "린스", 6000, "pet_shampoo"),
            ShoppingItem("이동가방", "켄넬", 15000, "carrier"),
            ShoppingItem("물티슈", "휴지", 3000, "wet_wipes"),
        ),
    )

    val all: List<ShoppingTheme> = listOf(cooking, tools, garden, pharmacy, stationery, pet)

    fun byId(id: String): ShoppingTheme = all.firstOrNull { it.id == id } ?: cooking
}
