package com.mose.seniorgame.data

import org.junit.Assert.assertEquals
import org.junit.Test

class TopicParticleTest {

    @Test
    fun `picks 은 after a final consonant and 는 otherwise`() {
        assertEquals("미역은", withTopicParticle("미역"))
        assertEquals("순두부는", withTopicParticle("순두부"))
        assertEquals("쪽파는", withTopicParticle("쪽파"))
        assertEquals("된장은", withTopicParticle("된장"))
    }
}
