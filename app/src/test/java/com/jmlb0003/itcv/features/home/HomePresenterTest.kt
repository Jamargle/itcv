package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomePresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<HomeViewState>(relaxed = true)
    private val usersRepository = mockk<UserRepository>(relaxed = true)
    private val getUserProfileUseCase = GetUserProfileUseCase(usersRepository)

    private val presenter = HomePresenter(viewState, getUserProfileUseCase, dispatchers)

    @Test
    fun `on init fetches profile info and displays if succeeded`() {
        val user = mockk<User>()
        every { usersRepository.getUser("Jamargle") } returns Either.Right(user)
        HomePresenter(viewState, getUserProfileUseCase, dispatchers)

        verify { viewState.displayUserInfo(user) }
    }

    @Test
    fun `on init fetches profile info and displays error if not succeeded`() {
        every { usersRepository.getUser("Jamargle") } returns Either.Left(Failure.NetworkConnection)
        HomePresenter(viewState, getUserProfileUseCase, dispatchers)

        verify { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
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