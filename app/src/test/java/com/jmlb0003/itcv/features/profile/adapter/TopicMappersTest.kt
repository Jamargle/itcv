package com.jmlb0003.itcv.features.profile.adapter

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import com.jmlb0003.itcv.domain.model.Topic as DomainTopic

class TopicMappersTest {

    private val topicsNormalizer = mockk<TopicNormalizer>()
    private val topicsMapper = TopicMappers(topicsNormalizer)

    @Test
    fun `on mapToPresentationItem maps given domain topic to a presentation topic`() {
        val name = "Topic 1"
        val topic = DomainTopic(
            name = name
        )
        val result = topicsMapper.mapToPresentationItem(topic)

        assertEquals(name, result.name)
    }

    @Test
    fun `on mapToPresentationItems maps given domain topics to a list of presentation topics`() {
        val name1 = "Topic 1"
        val name2 = "Topic 2"
        val topic1 = DomainTopic(
            name = name1
        )
        val topic2 = topic1.copy(name = name2)
        val topics = listOf(topic1, topic2)
        every { topicsNormalizer.getTopicNamesSortedByCount(topics) } returns listOf(
            topic1.name to 2,
            topic2.name to 1
        )
        val result = topicsMapper.mapToPresentationItems(topics)

        assertEquals(name1, result[0].name)
        assertEquals(name2, result[1].name)
    }
}
