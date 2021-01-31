package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.domain.model.User
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
class GetProfileDetailsUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val userRepository = mockk<UserRepository>()
    private val reposRepository = mockk<ReposRepository>()
    private val useCase = GetProfileDetailsUseCase(userRepository, reposRepository)

    @Test
    fun `on runUseCase with error from user repository returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { userRepository.getUser(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetProfileDetailsUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with error from repos repository returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { reposRepository.getUserRepositories(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetProfileDetailsUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result from user and repos repositories returns all the user's details`() =
        testDispatcher.runBlockingTest {
            val someUser = mockk<User>()
            val userRepositories: List<Repo> = listOf(mockk(), mockk())
            val username = "SomeUsername"
            every { userRepository.getUser(username) } returns Either.Right(someUser)
            every { reposRepository.getUserRepositories(username) } returns Either.Right(userRepositories)

            useCase(
                coroutineScope = this,
                dispatchers = dispatchers,
                params = GetProfileDetailsUseCase.Input(username)
            ) {
                with((it as Either.Right).rightValue) {
                    assertEquals(someUser, user)
                    assertEquals(userRepositories, repositories)
                }
            }
        }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { userRepository.getUser(any()) } returns Either.Right(mockk())
        every { reposRepository.getUserRepositories(any()) } returns Either.Right(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
