package com.jmlb0003.itcv.features.profile.adapter

import org.junit.Assert.assertEquals
import org.junit.Test
import com.jmlb0003.itcv.domain.model.Topic as DomainTopic

class TopicMappersTest {

    private val topicsMapper = TopicMappers

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
        val result = topicsMapper.mapToPresentationItems(listOf(topic1, topic2))

        assertEquals(name1, result[0].name)
        assertEquals(name2, result[1].name)
    }
}
