package com.jmlb0003.itcv.features.profile.adapter

import com.jmlb0003.itcv.domain.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicNormalizerTest {

    private val topicNormalizer = TopicNormalizer

    @Test
    fun `on getTopicNamesSortedByCount returns empty if given topics is empty`() {
        val topics = emptyList<Topic>()
        val result = topicNormalizer.getTopicNamesSortedByCount(topics)
        assertEquals(0, result.size)
    }

    @Test
    fun `on getTopicNamesSortedByCount returns list of pairs topic-name and count from given topics`() {
        val topics = listOf(
            Topic("aa"),
            Topic("Aa"),
            Topic("bb"),
            Topic("bb"),
            Topic("bb"),
            Topic("c")
        )
        val result = topicNormalizer.getTopicNamesSortedByCount(topics)
        assertEquals(3, result.size)
        assertEquals("Bb", result[0].first)
        assertEquals(3, result[0].second)
        assertEquals("Aa", result[1].first)
        assertEquals(2, result[1].second)
        assertEquals("C", result[2].first)
        assertEquals(1, result[2].second)
    }
}
