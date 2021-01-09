package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.UserRepository
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
class GetUserProfileUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val userRepository = mockk<UserRepository>()
    private val useCase = GetUserProfileUseCase(userRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `on runUseCase with error result returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { userRepository.getUser(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetUserProfileUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result returns the user's profile`() = testDispatcher.runBlockingTest {
        val someUser = getFakeUser()
        val username = "SomeUsername"
        every { userRepository.getUser(username) } returns Either.Right(someUser)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetUserProfileUseCase.Input(username)
        ) {
            assertEquals(someUser, (it as Either.Right).rightValue)
        }
    }

    private fun getFakeUser() = User(
        username = "",
        name = "",
        email = "",
        location = "",
        repositoryCount = -1
    )
}
