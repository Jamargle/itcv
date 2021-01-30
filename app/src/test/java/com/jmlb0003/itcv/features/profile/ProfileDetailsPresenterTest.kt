package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.Repo
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
    fun `on onViewReady displays profile name`() {
        val args = mockk<ProfileDetailsArgs>()
        val expectedUserName = "Some username"
        every { args.userName } returns expectedUserName
        every { args.memberSince } returns Date()

        presenter.onViewReady(args)

        verify { viewState.displayProfileName(expectedUserName) }
    }

    @Test
    fun `on onViewReady displays profile start date`() {
        val args = mockk<ProfileDetailsArgs>()
        val expectedMemberSince = Date(994560000000)
        every { args.userName } returns "Some username"
        every { args.memberSince } returns expectedMemberSince

        presenter.onViewReady(args)

        verify { viewState.displayMemberSince("08 - Jul - 2001") }
    }

    @Test
    fun `on onViewReady displays repositories information if the useCase fetches them successfully`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(
                userName = userName,
                memberSince = Date()
            )
            val expectedRepoName = "Repo name"
            val expectedRepoDescription = "Repo description"
            val expectedWebUrl = "Repo website"
            val expectedRepoUrl = "Repo url"
            val expectedRepository = Repo(expectedRepoName, expectedRepoDescription, expectedWebUrl, expectedRepoUrl)
            every { userRepository.getUser(userName) } returns Either.Right(mockk())
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(listOf(expectedRepository))

            presenter.onViewReady(args)

            val repositoriesToShow = slot<List<RepoListItem>>()
            verify { viewState.displayReposInformation(capture(repositoriesToShow)) }
            with(repositoriesToShow.captured[0]) {
                assertEquals(expectedRepoName, name)
                assertEquals(expectedRepoDescription, description)
                assertEquals(expectedWebUrl, website)
                assertEquals(expectedRepoUrl, repoUrl)
            }
        }

    @Test
    fun `on onViewReady does nothing for now if the useCase gets an error from repos repository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(
                userName = userName,
                memberSince = Date()
            )
            every { userRepository.getUser(userName) } returns Either.Right(mockk())
            every { reposRepository.getUserRepositories(userName) } returns Either.Left(Failure.NetworkConnection)

            presenter.onViewReady(args)

            verify(exactly = 0) { viewState.displayReposInformation(any()) }
        }

    @Test
    fun `on onViewReady does nothing for now if the useCase gets an error from userRepository`() =
        testDispatcher.runBlockingTest {
            val userName = "Some username"
            val args = ProfileDetailsArgs(
                userName = userName,
                memberSince = Date()
            )
            every { userRepository.getUser(userName) } returns Either.Left(Failure.NetworkConnection)
            every { reposRepository.getUserRepositories(userName) } returns Either.Right(emptyList())

            presenter.onViewReady(args)

            verify(exactly = 0) { viewState.displayReposInformation(any()) }
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
