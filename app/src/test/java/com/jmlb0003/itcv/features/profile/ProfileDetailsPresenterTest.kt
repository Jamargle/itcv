package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetProfileDetailsUseCase
import com.jmlb0003.itcv.features.profile.adapter.RepoListItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
class ProfileDetailsPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<ProfileDetailsViewState>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val reposRepository = mockk<ReposRepository>(relaxed = true)
    private val getProfileDetailsUseCase = GetProfileDetailsUseCase(userRepository, reposRepository)
    private val presenter = ProfileDetailsPresenter(viewState, getProfileDetailsUseCase, dispatchers)

    @Test
    fun `on onViewReady displays loading view while getting repositories`() {
        val args = ProfileDetailsArgs(user = null, userName = "")
        presenter.onViewReady(args)
        verify { viewState.displayLoadingRepos() }
    }

    @Test
    fun `on onViewReady displays repositories information if the useCase fetches them successfully`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(
                userName = userName
            )
            val expectedRepoName = "Repo name"
            val expectedRepoDescription = "Repo description"
            val expectedWebUrl = "Repo website"
            val expectedRepoUrl = "Repo url"
            val expectedRepository = Repo(expectedRepoName, expectedRepoDescription, expectedWebUrl, expectedRepoUrl)
            val expectedName = "Some name"
            val expectedBio = "Some biography"
            val expectedDate = Date(994560000000)
            val expectedEmail = "Some email"
            val expectedLocation = "Some location"
            val expectedRepositoryCount = 123
            val expectedFollowerCount = 456
            val expectedWebsite = "some website"
            val expectedTwitterAccount = "some twitter account"
            val expectedUser = getFakeUser().copy(
                username = userName,
                name = expectedName,
                bio = expectedBio,
                memberSince = expectedDate,
                email = expectedEmail,
                location = expectedLocation,
                repositoryCount = expectedRepositoryCount,
                followerCount = expectedFollowerCount,
                website = expectedWebsite,
                twitterAccount = expectedTwitterAccount
            )
            every { userRepository.getUser(userName) } returns Either.Right(expectedUser)
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(listOf(expectedRepository))

            presenter.onViewReady(args)

            val repositoriesToShow = slot<List<RepoListItem>>()
            verify {
                viewState.displayProfileName(expectedName)
                viewState.displayBio(expectedBio)
                viewState.displayEmail(expectedEmail)
                viewState.displayLocation(expectedLocation)
                viewState.displayFollowerCount(expectedFollowerCount.toString())
                viewState.displayWebsite(expectedWebsite)
                viewState.displayTwitterAccount(expectedTwitterAccount)
                viewState.displayReposInformation(capture(repositoriesToShow))
            }
            with(repositoriesToShow.captured[0]) {
                assertEquals(expectedRepoName, name)
                assertEquals(expectedRepoDescription, description)
                assertEquals(expectedWebUrl, website)
                assertEquals(expectedRepoUrl, repoUrl)
            }
        }

    @Test
    fun `on onViewReady displays user details when provided through profileDetailsArguments`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(
                user = getFakeUser().copy(
                    username = userName,
                    bio = "",
                    email = "",
                    location = "",
                    website = "",
                    twitterAccount = ""
                )
            )

            val expectedName = "Some name"
            val expectedBio = "Some biography"
            val expectedDate = Date(994560000000)
            val expectedEmail = "Some email"
            val expectedLocation = "Some location"
            val expectedRepositoryCount = 123
            val expectedFollowerCount = 456
            val expectedWebsite = "some website"
            val expectedTwitterAccount = "some twitter account"
            val expectedUser = getFakeUser().copy(
                username = userName,
                name = expectedName,
                bio = expectedBio,
                memberSince = expectedDate,
                email = expectedEmail,
                location = expectedLocation,
                repositoryCount = expectedRepositoryCount,
                followerCount = expectedFollowerCount,
                website = expectedWebsite,
                twitterAccount = expectedTwitterAccount
            )
            every { userRepository.getUser(userName) } returns Either.Right(expectedUser)
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(emptyList())

            presenter.onViewReady(args)

            verify {
                viewState.displayProfileName(expectedName)
                viewState.hideBio()
                viewState.hideEmail()
                viewState.hideLocation()
                viewState.displayFollowerCount(expectedFollowerCount.toString())
                viewState.hideWebsite()
                viewState.hideTwitterAccount()
            }
        }

    @Test
    fun `on onViewReady displays error if the useCase gets an error from repos repository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(userName = userName)
            every { userRepository.getUser(userName) } returns Either.Right(mockk())
            every { reposRepository.getUserRepositories(userName) } returns Either.Left(Failure.NetworkConnection)

            presenter.onViewReady(args)

            verify(exactly = 0) { viewState.displayReposInformation(any()) }
            verify { viewState.displayErrorMessage(null) }
        }

    @Test
    fun `on onViewReady hides loading view for repositories if the useCase gets an error from repos repository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(userName = userName)
            every { userRepository.getUser(userName) } returns Either.Right(mockk())
            every { reposRepository.getUserRepositories(userName) } returns Either.Left(Failure.NetworkConnection)

            presenter.onViewReady(args)

            verify { viewState.hideRepos() }
        }

    @Test
    fun `on onViewReady displays error if the useCase gets an error from userRepository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(userName = userName)
            every { userRepository.getUser(userName) } returns Either.Left(Failure.NetworkConnection)
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(emptyList())

            presenter.onViewReady(args)

            verify(exactly = 0) { viewState.displayReposInformation(any()) }
            verify { viewState.displayErrorMessage(null) }
        }

    @Test
    fun `on onViewReady hides loading view for repositories if the useCase gets an error from userRepository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(userName = userName)
            every { userRepository.getUser(userName) } returns Either.Left(Failure.NetworkConnection)
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(emptyList())

            presenter.onViewReady(args)

            verify { viewState.hideRepos() }
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

    private fun getFakeUser() =
        User(
            username = "",
            name = "",
            memberSince = Date()
        )
}
