package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.domain.repositories.ReposRepository
import com.jmlb0003.itcv.domain.repositories.TopicsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserTopicsUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)
    private val reposRepository = mockk<ReposRepository>()
    private val topicsRepository = mockk<TopicsRepository>()

    private val useCase = GetUserTopicsUseCase(reposRepository, topicsRepository)

    @Test
    fun `on runUseCase with error from reposRepository returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { reposRepository.getUserRepositories(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetUserTopicsUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result from repos and topics repositories returns all the user topics sorted by number of occurrences`() =
        testDispatcher.runBlockingTest {

            val username = "SomeUsername"
            val repo1 = "Repository1"
            val topics1 = listOf(
                getFakeTopic().copy(name = "A"),
                getFakeTopic().copy(name = "B"),
                getFakeTopic().copy(name = "C"),
            )
            val repo2 = "Repository2"
            val topics2 = listOf(
                getFakeTopic().copy(name = "c"), // it will be counted with 'C', but as different topic, it is in the list as well, but at the end
                getFakeTopic().copy(name = "D"),
                getFakeTopic().copy(name = "E"),
            )
            val repo3 = "Repository3"
            val topics3 = listOf(
                getFakeTopic().copy(name = "a"), // a will be counted as 'A' but it goes at the end of the list because it is considered as a different topic
                getFakeTopic().copy(name = "B"),
                getFakeTopic().copy(name = "C"),
            )
            val userRepositories = listOf(
                getFakeRepo().copy(name = repo1),
                getFakeRepo().copy(name = repo2),
                getFakeRepo().copy(name = repo3)
            )
            every { reposRepository.getUserRepositories(username) } returns Either.Right(userRepositories)
            every { topicsRepository.getRepositoryTopics(repo1, username) } returns Either.Right(topics1)
            every { topicsRepository.getRepositoryTopics(repo2, username) } returns Either.Right(topics2)
            every { topicsRepository.getRepositoryTopics(repo3, username) } returns Either.Right(topics3)

            useCase(
                coroutineScope = this,
                dispatchers = dispatchers,
                params = GetUserTopicsUseCase.Input(username)
            ) {
                with((it as Either.Right).rightValue) {
                    assertEquals(9, size)
                    assertEquals("A", get(0).name)
                    assertEquals("B", get(1).name)
                    assertEquals("C", get(2).name)
                    assertEquals("c", get(3).name)
                    assertEquals("D", get(4).name)
                    assertEquals("E", get(5).name)
                    assertEquals("a", get(6).name)
                    assertEquals("B", get(7).name)
                    assertEquals("C", get(8).name)
                }
            }
        }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { reposRepository.getUserRepositories(any()) } returns Either.Right(emptyList())
        every { topicsRepository.getRepositoryTopics(any(), any()) } returns Either.Right(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun getFakeRepo() = Repo(
        id = "",
        name = "",
        description = "",
        website = "",
        repoUrl = "",
        starsCount = -1,
        watchersCount = -1,
        forksCount = -1
    )

    private fun getFakeTopic() = Topic("")
}
