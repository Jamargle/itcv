package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.MissingDefaultUserNameFailure
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.features.MainToolbarController
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
import java.util.Date

@ExperimentalCoroutinesApi
class HomePresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<HomeViewState>(relaxed = true)
    private val toolbarController = mockk<MainToolbarController>(relaxed = true)
    private val navigationTriggers = mockk<NavigationTriggers>(relaxed = true)
    private val usersRepository = mockk<UserRepository>(relaxed = true)
    private val getUserProfileUseCase = GetDefaultUserProfileUseCase(usersRepository)

    @Test
    fun `on init displays loading while fetching profile info`() {
        createHomePresenter()
        verify { viewState.displayLoading() }
    }

    @Test
    fun `on init hides loading after fetching profile info with success`() {
        every { usersRepository.getDefaultUser() } returns Either.Right(mockk(relaxed = true))
        createHomePresenter()
        verify { viewState.hideLoading() }
    }

    @Test
    fun `on init hides loading after fetching profile info with error`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(mockk(relaxed = true))
        createHomePresenter()
        verify { viewState.hideLoading() }
    }

    @Test
    fun `on init fetches profile info and if success then updates the title with the user name`() {
        val userName = "Some user name"
        val user = getFakeUser().copy(username = userName)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { toolbarController.setNewTitle(userName) }
    }

    @Test
    fun `on init fetches profile info and if success then displays the user name`() {
        val userName = "Some name"
        val user = getFakeUser().copy(name = userName)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayProfileName(userName) }
    }

    @Test
    fun `on init fetches profile info and if success and bio not empty then displays the bio`() {
        val bio = "Some bio for the user"
        val user = getFakeUser().copy(bio = bio)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayBio(bio) }
    }

    @Test
    fun `on init fetches profile info and if success and bio empty then hides the bio`() {
        val bio = ""
        val user = getFakeUser().copy(bio = bio)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.hideBio() }
    }

    @Test
    fun `on init fetches profile info and if success and email not empty then displays the email`() {
        val email = "Some email"
        val user = getFakeUser().copy(email = email)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayEmail(email) }
    }

    @Test
    fun `on init fetches profile info and if success and email empty then hides the email`() {
        val email = ""
        val user = getFakeUser().copy(email = email)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.hideEmail() }
    }

    @Test
    fun `on init fetches profile info and if success and location not empty then displays the location`() {
        val location = "Some location"
        val user = getFakeUser().copy(location = location)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayLocation(location) }
    }

    @Test
    fun `on init fetches profile info and if success and location empty then hides the location`() {
        val location = ""
        val user = getFakeUser().copy(location = location)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.hideLocation() }
    }

    @Test
    fun `on init fetches profile info and if success then displays the public repository count`() {
        val count = 123
        val user = getFakeUser().copy(repositoryCount = count)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayRepositoryCount(count.toString()) }
    }

    @Test
    fun `on init fetches profile info and if success then displays the github follower count`() {
        val count = 123
        val user = getFakeUser().copy(followerCount = count)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayFollowerCount(count.toString()) }
    }

    @Test
    fun `on init fetches profile info and if success and website not empty then displays the website`() {
        val website = "Some website"
        val user = getFakeUser().copy(website = website)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayWebsite(website) }
    }

    @Test
    fun `on init fetches profile info and if success and website empty then hides the website`() {
        val website = ""
        val user = getFakeUser().copy(website = website)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.hideWebsite() }
    }

    @Test
    fun `on init fetches profile info and if success and twitterAccount not empty then displays the twitterAccount`() {
        val twitterAccount = "Some twitter account"
        val user = getFakeUser().copy(twitterAccount = twitterAccount)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.displayTwitterAccount(twitterAccount) }
    }

    @Test
    fun `on init fetches profile info and if success and twitterAccount empty then hides the twitterAccount`() {
        val twitterAccount = ""
        val user = getFakeUser().copy(twitterAccount = twitterAccount)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        createHomePresenter()

        verify { viewState.hideTwitterAccount() }
    }

    @Test
    fun `on init fetches profile info and displays error if not succeeded`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(Failure.NetworkConnection)
        createHomePresenter()

        verify { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
    }

    @Test
    fun `on init fetches profile info and displays the dialog to add default user if there is no default username`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(MissingDefaultUserNameFailure)
        createHomePresenter()

        verify { viewState.displayEnterUsernameDialog() }
    }

    @Test
    fun `on onDefaultUsernameChange displays loading while fetching profile info`() {
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()
        verify(exactly = 2) { viewState.displayLoading() }
    }

    @Test
    fun `on onDefaultUsernameChange hides loading after fetching profile info with success`() {
        every { usersRepository.getDefaultUser() } returns Either.Right(mockk(relaxed = true))
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()
        verify(exactly = 2) { viewState.hideLoading() }
    }

    @Test
    fun `on onDefaultUsernameChange hides loading after fetching profile info with error`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(mockk(relaxed = true))
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()
        verify(exactly = 2) { viewState.hideLoading() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success then updates the title with the user name`() {
        val userName = "Some user name"
        val user = getFakeUser().copy(username = userName)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { toolbarController.setNewTitle(userName) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success then displays the user name`() {
        val userName = "Some name"
        val user = getFakeUser().copy(name = userName)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayProfileName(userName) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and bio not empty then displays the bio`() {
        val bio = "Some bio for the user"
        val user = getFakeUser().copy(bio = bio)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayBio(bio) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and bio empty then hides the bio`() {
        val bio = ""
        val user = getFakeUser().copy(bio = bio)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()
        verify { viewState.hideBio() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and email not empty then displays the email`() {
        val email = "Some email"
        val user = getFakeUser().copy(email = email)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayEmail(email) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and email empty then hides the email`() {
        val email = ""
        val user = getFakeUser().copy(email = email)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()
        verify { viewState.hideEmail() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and location not empty then displays the location`() {
        val location = "Some location"
        val user = getFakeUser().copy(location = location)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayLocation(location) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and location empty then hides the location`() {
        val location = ""
        val user = getFakeUser().copy(location = location)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.hideLocation() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success then displays the public repository count`() {
        val count = 123
        val user = getFakeUser().copy(repositoryCount = count)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayRepositoryCount(count.toString()) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success then displays the github follower count`() {
        val count = 123
        val user = getFakeUser().copy(followerCount = count)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayFollowerCount(count.toString()) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and website not empty then displays the website`() {
        val website = "Some website"
        val user = getFakeUser().copy(website = website)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayWebsite(website) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and website empty then hides the website`() {
        val website = ""
        val user = getFakeUser().copy(website = website)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.hideWebsite() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and twitterAccount not empty then displays the twitterAccount`() {
        val twitterAccount = "Some twitter account"
        val user = getFakeUser().copy(twitterAccount = twitterAccount)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayTwitterAccount(twitterAccount) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and if success and twitterAccount empty then hides the twitterAccount`() {
        val twitterAccount = ""
        val user = getFakeUser().copy(twitterAccount = twitterAccount)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.hideTwitterAccount() }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and displays error if not succeeded`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(Failure.NetworkConnection)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
    }

    @Test
    fun `on onDefaultUsernameChange fetches profile info and displays the dialog to add default user if there is no default username`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(MissingDefaultUserNameFailure)
        val presenter = createHomePresenter()
        presenter.onDefaultUsernameChange()

        verify { viewState.displayEnterUsernameDialog() }
    }

    @Test
    fun `on onSeeAllClicked with current user null does nothing`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(Failure.NetworkConnection)
        val presenter = createHomePresenter()
        presenter.onSeeAllClicked()

        verify(exactly = 1) { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
        verify { navigationTriggers wasNot Called }
    }

    @Test
    fun `on onSeeAllClicked with current user not null requests navigation to profile details with that user`() {
        val user = getFakeUser()
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onSeeAllClicked()

        verify { navigationTriggers.navigateToProfileDetails(user) }
    }

    @Test
    fun `on onUserWebsiteClicked with current user null does nothing`() {
        every { usersRepository.getDefaultUser() } returns Either.Left(Failure.NetworkConnection)
        val presenter = createHomePresenter()
        presenter.onUserWebsiteClicked()

        verify(exactly = 1) { viewState.displayErrorMessage(R.string.error_dialog_no_network) }
        verify { navigationTriggers wasNot Called }
    }

    @Test
    fun `on onUserWebsiteClicked with current user not null requests navigation to profile details with that user`() {
        val website = "www.some_website.com"
        val user = getFakeUser().copy(website = website)
        every { usersRepository.getDefaultUser() } returns Either.Right(user)
        val presenter = createHomePresenter()
        presenter.onUserWebsiteClicked()

        verify { navigationTriggers.openUrl(website) }
    }

    private fun createHomePresenter() =
        HomePresenter(viewState, toolbarController, navigationTriggers, getUserProfileUseCase, dispatchers)

    private fun getFakeUser() = User(username = "", name = "", memberSince = Date())

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