package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.topic.TopicsService
import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.domain.model.Topic
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class TopicsRepositoryTest {

    private val topicsService = mockk<TopicsService>(relaxed = true)
    private val topicsMappers = mockk<TopicsMapper>(relaxed = true)

    private val repository = TopicsRepository(topicsService, topicsMappers)

    @Test
    fun `on getRepositoryTopics if service returns failure returns the error`() {
        val error = Failure.NetworkConnection
        every { topicsService.getTopics(any(), any()) } returns Either.Left(error)

        val result = repository.getRepositoryTopics("", "")

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getRepositoryTopics if service returns right topics response it converts them to domain model and returns them`() {
        val repoOwner = "Some username"
        val repoName = "Some repository name"
        val topicName1 = "Topic1"
        val topicName2 = "Topic2"
        val topicsResponse = TopicsResponse(listOf(topicName1, topicName2))
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(topicsResponse)
        val topic1 = Topic(topicName1)
        val topic2 = Topic(topicName2)
        val mappedTopics = listOf(topic1, topic2)
        every { topicsMappers.mapToDomain(topicsResponse) } returns mappedTopics

        val result = repository.getRepositoryTopics(repoName, repoOwner)

        assertEquals(topic1, (result as Either.Right).rightValue[0])
        assertEquals(topic2, result.rightValue[1])
    }
}
