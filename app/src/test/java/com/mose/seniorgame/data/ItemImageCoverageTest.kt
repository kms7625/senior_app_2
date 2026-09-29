package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * 테마 데이터의 모든 목표·미끼 slug에 실제 PNG가 번들돼 있는지 확인한다.
 * Gradle 단위 테스트의 작업 디렉터리는 모듈(app/) 폴더다.
 */
class ItemImageCoverageTest {

    private val imageDir = File("src/main/res/drawable-nodpi")
    private val allItems = ThemePool.all.flatMap { it.items }

    @Test
    fun `every target item has an ic_item png`() {
        val missing = allItems.map { "ic_item_${it.iconSlug}.png" }.filterNot { File(imageDir, it).isFile }
        assertTrue("없는 목표 그림: $missing", missing.isEmpty())
    }

    @Test
    fun `every decoy has an ic_decoy png`() {
        val missing = allItems.map { "ic_decoy_${it.decoyIconSlug}.png" }.filterNot { File(imageDir, it).isFile }
        assertTrue("없는 미끼 그림: $missing", missing.isEmpty())
    }

    @Test
    fun `decoy image slugs are all distinct`() {
        assertEquals(60, allItems.map { it.decoyIconSlug }.toSet().size)
    }

    @Test
    fun `decoyIconOf finds the decoy image within a theme`() {
        assertEquals("silken_tofu", ThemePool.cooking.decoyIconOf("순두부"))
    }
}
