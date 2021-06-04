package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.Topic
import com.jmlb0003.itcv.domain.exception.NoInsertedTopicsException
import com.jmlb0003.itcv.domain.exception.NoTopicsException
import com.jmlb0003.itcv.domain.exception.NoTopicsRemovedException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TopicsLocalDataSourceTest {

    private val database = mockk<MyDataBase>()
    private val topicsLocalDataSource = TopicsLocalDataSource(database)

    @Test
    fun `on saveTopics returns error if the repos cannot be inserted in the database`() {
        val topic = mockk<Topic>()
        val topics = listOf(topic)
        val expectedError = "Some error message"
        every { database.topicsDao().insertTopics(topics) } throws IllegalStateException(expectedError)

        val result = topicsLocalDataSource.saveTopics(topics)

        assertTrue((result as Either.Left).leftValue is NoInsertedTopicsException)
        assertEquals("Not possible to insert topics because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on saveTopics returns Unit from database if properly inserted`() {
        val topic = mockk<Topic>()
        val topics = listOf(topic)
        every { database.topicsDao().insertTopics(topics) } returns Unit

        val result = topicsLocalDataSource.saveTopics(topics)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }

    @Test
    fun `on getTopicsByRepo returns error if there is a exception thrown by the database`() {
        val repoId = "Some Repo ID"
        val expectedError = "Some error message"
        every { database.topicsDao().getTopicsByRepo(repoId) } throws NullPointerException(expectedError)

        val result = topicsLocalDataSource.getTopicsByRepo(repoId)

        assertTrue((result as Either.Left).leftValue is NoTopicsException)
        assertTrue(result.leftValue.error is IllegalStateException)
        assertEquals("There are no topics because of: $expectedError", result.leftValue.error?.message)
    }

    @Test
    fun `on getTopicsByRepo returns the topics from database if found`() {
        val repoId = "Some Repo ID"
        val expectedTopics = mockk<List<Topic>>()
        every { database.topicsDao().getTopicsByRepo(repoId) } returns expectedTopics

        val result = topicsLocalDataSource.getTopicsByRepo(repoId)

        assertEquals(expectedTopics, (result as Either.Right).rightValue)
    }

    @Test
    fun `on removeTopicsByRepo returns error if no topics found in the database`() {
        val repoId = "Some Repo ID"
        every { database.topicsDao().getTopicsByRepo(repoId) } returns emptyList()

        val result = topicsLocalDataSource.removeTopicsByRepo(repoId)

        assertTrue((result as Either.Left).leftValue is NoTopicsRemovedException)
    }

    @Test
    fun `on removeTopicsByRepo returns error if error when looking for existing topics in the database`() {
        val repoId = "Some Repo ID"
        every { database.topicsDao().getTopicsByRepo(repoId) } throws IllegalStateException()

        val result = topicsLocalDataSource.removeTopicsByRepo(repoId)

        assertTrue((result as Either.Left).leftValue is NoTopicsRemovedException)
    }

    @Test
    fun `on removeTopicsByRepo returns error if the topics could not be removed from the database`() {
        val repoId = "Some Repo ID"
        val expectedTopics = mockk<List<Topic>>()
        every { database.topicsDao().getTopicsByRepo(repoId) } returns expectedTopics
        every { database.topicsDao().removeTopics(expectedTopics) } throws IllegalStateException()

        val result = topicsLocalDataSource.removeTopicsByRepo(repoId)

        assertTrue((result as Either.Left).leftValue is NoTopicsRemovedException)
    }

    @Test
    fun `on removeTopicsByRepo returns Unit if the topics are successfully removed from database`() {
        val repoId = "Some Repo ID"
        val expectedTopics = mockk<List<Topic>>()
        every { database.topicsDao().getTopicsByRepo(repoId) } returns expectedTopics
        every { database.topicsDao().removeTopics(expectedTopics) } returns Unit

        val result = topicsLocalDataSource.removeTopicsByRepo(repoId)

        assertEquals(Unit, (result as Either.Right).rightValue)
    }
}
