package com.jmlb0003.itcv.features.input

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.domain.usecases.UpdateDefaultUserUseCase
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
class InsertUserDialogPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<InsertUserDialogViewState>(relaxed = true)
    private val userRepository = mockk<UserRepository>()
    private val getDefaultUserProfileUseCase = GetDefaultUserProfileUseCase(userRepository)
    private val updateDefaultUserUseCase = UpdateDefaultUserUseCase(userRepository)

    private val presenter = InsertUserDialogPresenter(
        viewState,
        getDefaultUserProfileUseCase,
        updateDefaultUserUseCase,
        dispatchers
    )

    @Test
    fun `on onViewPrepared fetches the current default username and displays empty username on error`() {
        every { userRepository.getDefaultUser() } returns Either.Left(Failure.NetworkConnection)
        presenter.onViewPrepared()
        verify { viewState.displayCurrentUsername("") }
    }

    @Test
    fun `on onViewPrepared fetches the current default username and displays it on success`() {
        val username = "Some username"
        val user = mockk<User>(relaxed = true)
        every { user.username } returns username
        every { userRepository.getDefaultUser() } returns Either.Right(user)

        presenter.onViewPrepared()

        verify { viewState.displayCurrentUsername(username) }
    }

    @Test
    fun `on onTextChange with null text disables the 'Done' button`() {
        presenter.onTextChange(null)
        verify { viewState.disableDoneButton() }
    }

    @Test
    fun `on onTextChange with blank text disables the 'Done' button`() {
        presenter.onTextChange(" ")
        verify { viewState.disableDoneButton() }
    }

    @Test
    fun `on onTextChange with same text as current username disables the 'Done' button`() {
        val currentUsername = "username"
        mockCurrentUsername(currentUsername = currentUsername)

        presenter.onTextChange(currentUsername)

        verify { viewState.disableDoneButton() }
    }

    @Test
    fun `on onTextChange with different text than current username enables the 'Done' button`() {
        val currentUsername = "username"
        mockCurrentUsername(currentUsername = currentUsername)

        presenter.onTextChange("Other username")

        verify { viewState.enableDoneButton() }
    }

    @Test
    fun `on onDoneClicked disables 'Done' button to avoid more clicks`() {
        presenter.onDoneClicked("")
        verify { viewState.disableDoneButton() }
    }

    @Test
    fun `on onDoneClicked with username equals to current username enables the 'Done' button`() {
        val currentUsername = "username1"
        mockCurrentUsername(currentUsername = currentUsername)

        presenter.onDoneClicked(currentUsername)

        verify { viewState.enableDoneButton() }
    }

    @Test
    fun `on onDoneClicked with username null enables the 'Done' button`() {
        val newUsername = null
        presenter.onDoneClicked(newUsername)
        verify { viewState.enableDoneButton() }
    }

    @Test
    fun `on onDoneClicked with username blank enables the 'Done' button`() {
        val newUsername = "  "
        presenter.onDoneClicked(newUsername)
        verify { viewState.enableDoneButton() }
    }

    @Test
    fun `on onDoneClicked with username different than current one and not blank or null updates current username and dismiss the view on error`() {
        val currentUsername = "username1"
        val newUsername = "Username 2"
        mockCurrentUsername(currentUsername = currentUsername)
        every { userRepository.updateDefaultUser(newUsername) } returns Either.Left(Failure.NetworkConnection)

        presenter.onDoneClicked(newUsername)

        verify { viewState.leaveView() }
    }

    @Test
    fun `on onDoneClicked with username different than current one and not blank or null updates current username and dismiss the view on success`() {
        val currentUsername = "Username 1"
        val newUsername = "Username 2"
        mockCurrentUsername(currentUsername = currentUsername)
        every { userRepository.updateDefaultUser(newUsername) } returns Either.Right(newUsername)

        presenter.onDoneClicked(newUsername)

        verify { viewState.leaveView(newUsername) }
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

    private fun mockCurrentUsername(currentUsername: String = "") {
        val user = mockk<User>(relaxed = true)
        every { user.username } returns currentUsername
        every { userRepository.getDefaultUser() } returns Either.Right(user)

        presenter.onViewPrepared()
    }
}
