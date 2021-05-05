package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.repositories.UserRepository
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
class SearchUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val userRepository = mockk<UserRepository>()
    private val useCase = SearchUseCase(userRepository)

    @Test
    fun `on runUseCase with error result returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { userRepository.searchUserByUsername(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = SearchUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result returns the list of results`() = testDispatcher.runBlockingTest {
        val expectedResults = mockk<List<SearchResult>>()
        val username = "SomeUsername"
        every { userRepository.searchUserByUsername(username) } returns Either.Right(expectedResults)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = SearchUseCase.Input(username)
        ) {
            assertEquals(expectedResults, (it as Either.Right).rightValue)
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}