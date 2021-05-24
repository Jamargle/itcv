package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicsMapperTest {

    private val mapper = TopicsMapper

    @Test
    fun `on mapToDomain converts topics API data object to domain topics`() {
        val apiTopic1 = "t1"
        val apiTopic2 = "t2"
        val response = TopicsResponse(listOf(apiTopic1, apiTopic2))
        val result = mapper.mapToDomain(response)

        assertEquals(apiTopic1, result[0].name)
        assertEquals(apiTopic2, result[1].name)
    }
}
