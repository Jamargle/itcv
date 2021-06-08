package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import com.jmlb0003.itcv.domain.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import com.jmlb0003.itcv.data.model.Topic as DataTopic

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

    @Test
    fun `mapToDomain converts a DataTopic to a domain model topic`() {
        val expectedName = "some name"
        val expectedRepoId = "some id"
        val dataTopic = DataTopic(
            name = expectedName,
            lastCacheUpdate = 0,
            relatedRepo = expectedRepoId
        )

        val result = mapper.mapToDomain(dataTopic)

        with(result) {
            assertEquals(expectedName, name)
        }
    }

    @Test
    fun `mapToData with domain Topic converts it to a data model object`() {
        val expectedName = "some name"
        val expectedUpdatedDate = 4560000000123
        val expectedRepoId = "Some repo ID"
        val topic = Topic(
            name = expectedName
        )

        val result = mapper.mapToData(
            topic,
            expectedRepoId,
            Date(expectedUpdatedDate)
        )

        with(result) {
            assertEquals(expectedName, name)
            assertEquals(expectedRepoId, relatedRepo)
            assertEquals(expectedUpdatedDate, lastCacheUpdate)
        }
    }
}
