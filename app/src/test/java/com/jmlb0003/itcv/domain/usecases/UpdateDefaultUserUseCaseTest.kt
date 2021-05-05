package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateDefaultUserUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val userRepository = mockk<UserRepository>()
    private val useCase = UpdateDefaultUserUseCase(userRepository)

    @Test
    fun `on run usecase with error from user repository returns the error`() = testDispatcher.runBlockingTest {
        val someFailure = Failure.NetworkConnection
        val username = "SomeUsername"
        every { userRepository.updateDefaultUser(username) } returns Either.Left(someFailure)

        useCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = UpdateDefaultUserUseCase.Input(username)
        ) {
            assertEquals(someFailure, (it as Either.Left).leftValue)
        }
    }

    @Test
    fun `on runUseCase with success result from user repository returns the new username`() =
        testDispatcher.runBlockingTest {
            val username = "SomeUsername"
            val newUsername = "Other Username"
            every { userRepository.updateDefaultUser(username) } returns Either.Right(newUsername)

            useCase(
                coroutineScope = this,
                dispatchers = dispatchers,
                params = UpdateDefaultUserUseCase.Input(username)
            ) {
                assertTrue(it is Either.Right)
                assertEquals(newUsername, (it as Either.Right).rightValue)
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
