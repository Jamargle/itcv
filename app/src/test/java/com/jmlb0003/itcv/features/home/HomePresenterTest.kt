package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase
import io.mockk.Called
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
    private val navigationTriggers = mockk<NavigationTriggers>(relaxed = true)
    private val usersRepository = mockk<UserRepository>(relaxed = true)
    private val getUserProfileUseCase = GetUserProfileUseCase(usersRepository)

    @Test
    fun `on init fetches profile info and displays if succeeded`() {
        val user = mockk<User>()
        every { usersRepository.getUser("Jamargle") } returns Either.Right(user)
        HomePresenter(viewState, navigationTriggers, getUserProfileUseCase, dispatchers)

        verify { viewState.displayUserInfo(user) }
    }

    @Test
    fun `on init fetches profile info and displays error if not succeeded`() {
        every { usersRepository.getUser("Jamargle") } returns Either.Left(Failure.NetworkConnection)
        HomePresenter(viewState, navigationTriggers, getUserProfileUseCase, dispatchers)

        verify { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
    }

    @Test
    fun `on onSeeAllClicked with current user null does nothing`() {
        every { usersRepository.getUser("Jamargle") } returns Either.Left(Failure.NetworkConnection)
        val presenter = HomePresenter(viewState, navigationTriggers, getUserProfileUseCase, dispatchers)
        presenter.onSeeAllClicked()

        verify(exactly = 1) { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
        verify { navigationTriggers wasNot Called }
    }

    @Test
    fun `on onSeeAllClicked with current user not null requests navigation to profile details with that user`() {
        val user = mockk<User>()
        every { usersRepository.getUser("Jamargle") } returns Either.Right(user)
        val presenter = HomePresenter(viewState, navigationTriggers, getUserProfileUseCase, dispatchers)
        presenter.onSeeAllClicked()

        verify { navigationTriggers.navigateToProfileDetails(user) }
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