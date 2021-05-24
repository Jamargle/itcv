package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.domain.model.User
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
import java.util.Date

@ExperimentalCoroutinesApi
class GetDefaultUserProfileUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val userRepository = mockk<UserRepository>()
    private val useCase = GetDefaultUserProfileUseCase(userRepository)

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
        every { userRepository.getDefaultUser() } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = UseCase.None
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result returns the default user's profile`() = testDispatcher.runBlockingTest {
        val someUser = getFakeUser()
        every { userRepository.getDefaultUser() } returns Either.Right(someUser)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = UseCase.None
        ) {
            assertEquals(someUser, (it as Either.Right).rightValue)
        }
    }

    private fun getFakeUser() = User(
        avatarUrl = "",
        username = "",
        name = "",
        memberSince = Date(),
        email = "",
        location = "",
        repositoryCount = -1
    )
}
