package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.TopicsLocalDataSource
import com.jmlb0003.itcv.data.network.topic.TopicsService
import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.domain.model.Topic
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date
import com.jmlb0003.itcv.data.model.Topic as DataTopic

class TopicsRepositoryTest {

    private val topicsService = mockk<TopicsService>(relaxed = true)
    private val topicsMappers = mockk<TopicsMapper>(relaxed = true)
    private val topicsLocalDataSource = mockk<TopicsLocalDataSource>(relaxed = true)

    private val repository = TopicsRepository(topicsLocalDataSource, topicsService, topicsMappers)

    @Test
    fun `on getRepositoryTopics returns cached topics if exist and are valid`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        val expectedDataTopic = mockk<DataTopic>()
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Right(listOf(expectedDataTopic))
        val expectedDomainTopic = mockk<Topic>()
        every { expectedDataTopic.lastCacheUpdate } returns Date().time
        every { topicsMappers.mapToDomain(expectedDataTopic) } returns expectedDomainTopic

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(expectedDomainTopic, (result as Either.Right).rightValue[0])
    }

    @Test
    fun `on getRepositoryTopics removes cached topics if no longer valid`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        val expectedDataTopic = mockk<DataTopic>()
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Right(listOf(expectedDataTopic))
        every { expectedDataTopic.lastCacheUpdate } returns
                Calendar.getInstance().apply { add(Calendar.HOUR, -25) }.timeInMillis
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(mockk())

        repository.getRepositoryTopics(repoId, repoName, repoOwner)

        verify { topicsLocalDataSource.removeTopicsByRepo(repoId) }
        verify { topicsMappers.mapToDomain(any<DataTopic>()) wasNot called }
    }

    @Test
    fun `on getRepositoryTopics fetches topics from service if no cached topics found`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Right(emptyList())
        val expectedTopicsResponse = mockk<TopicsResponse>()
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(expectedTopicsResponse)
        val expectedDomainTopic = mockk<Topic>()
        every { topicsMappers.mapToDomain(expectedTopicsResponse) } returns listOf(expectedDomainTopic)

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(expectedDomainTopic, (result as Either.Right).rightValue[0])
        verify { topicsService.getTopics(repoOwner, repoName) }
    }

    @Test
    fun `on getRepositoryTopics fetches topics from service if error when looking for cached topics`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Left(Failure.NetworkConnection)
        val expectedTopicsResponse = mockk<TopicsResponse>()
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(expectedTopicsResponse)
        val expectedDomainTopic = mockk<Topic>()
        every { topicsMappers.mapToDomain(expectedTopicsResponse) } returns listOf(expectedDomainTopic)

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(expectedDomainTopic, (result as Either.Right).rightValue[0])
        verify { topicsService.getTopics(repoOwner, repoName) }
    }

    @Test
    fun `on getRepositoryTopics fetches topics from service if cached topics are not valid`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        val expectedDataTopic = mockk<DataTopic>()
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Right(listOf(expectedDataTopic))
        every { expectedDataTopic.lastCacheUpdate } returns
                Calendar.getInstance().apply { add(Calendar.HOUR, -25) }.timeInMillis
        val expectedTopicsResponse = mockk<TopicsResponse>()
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(expectedTopicsResponse)
        val expectedDomainTopic = mockk<Topic>()
        every { topicsMappers.mapToDomain(expectedTopicsResponse) } returns listOf(expectedDomainTopic)

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(expectedDomainTopic, (result as Either.Right).rightValue[0])
        verify { topicsService.getTopics(repoOwner, repoName) }
    }

    @Test
    fun `on getRepositoryTopics without cached topics if service returns failure returns the error`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns
                Either.Left(Failure.DatabaseError(NullPointerException()))
        val error = Failure.NetworkConnection
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Left(error)

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(error, (result as Either.Left).leftValue)
    }

    @Test
    fun `on getRepositoryTopics without cached topics if service returns error response does nothing else`() {
        val repoId = "Some repo ID"
        val repoName = "Some repo name"
        val repoOwner = "Some user ID"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns
                Either.Left(Failure.DatabaseError(NullPointerException()))
        val error = Failure.NetworkConnection
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Left(error)

        repository.getRepositoryTopics(repoId, repoName, repoOwner)

        verify { topicsMappers.mapToData(any(), any(), any()) wasNot called }
        verify { topicsLocalDataSource.saveTopics(any()) wasNot called }
    }

    @Test
    fun `on getRepositoryTopics without valid cached topics if service returns right topics response it converts them to domain model and returns them`() {
        val repoId = "Some repo ID"
        val repoOwner = "Some user ID"
        val repoName = "Some repository name"
        val topicName1 = "Topic1"
        val topicName2 = "Topic2"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Left(Failure.NetworkConnection)
        val topicsResponse = TopicsResponse(listOf(topicName1, topicName2))
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(topicsResponse)
        val topic1 = Topic(topicName1)
        val topic2 = Topic(topicName2)
        val mappedTopics = listOf(topic1, topic2)
        every { topicsMappers.mapToDomain(topicsResponse) } returns mappedTopics

        val result = repository.getRepositoryTopics(repoId, repoName, repoOwner)

        assertEquals(topic1, (result as Either.Right).rightValue[0])
        assertEquals(topic2, result.rightValue[1])
    }

    @Test
    fun `on getRepositoryTopics without cached topics if service returns right topics response caches the new topics`() {
        val repoId = "Some repo ID"
        val repoOwner = "Some user ID"
        val repoName = "Some repository name"
        every { topicsLocalDataSource.getTopicsByRepo(repoId) } returns Either.Left(Failure.NetworkConnection)
        val topicsResponse = mockk<TopicsResponse>()
        val topic1 = mockk<Topic>()
        val topic2 = mockk<Topic>()
        every { topicsService.getTopics(repoOwner, repoName) } returns Either.Right(topicsResponse)
        every { topicsMappers.mapToDomain(topicsResponse) } returns listOf(topic1, topic2)
        val dataTopic1 = mockk<DataTopic>()
        val dataTopic2 = mockk<DataTopic>()
        every { topicsMappers.mapToData(topic1, repoId, any()) } returns dataTopic1
        every { topicsMappers.mapToData(topic2, repoId, any()) } returns dataTopic2

        repository.getRepositoryTopics(repoId, repoName, repoOwner)

        verify { topicsLocalDataSource.saveTopics(listOf(dataTopic1, dataTopic2)) }
    }
}
