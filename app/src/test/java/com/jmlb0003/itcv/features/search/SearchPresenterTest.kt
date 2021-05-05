package com.jmlb0003.itcv.features.search

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.coroutines.TestDispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.repositories.UserRepository
import com.jmlb0003.itcv.domain.usecases.SearchUseCase
import com.jmlb0003.itcv.features.home.NavigationTriggers
import com.jmlb0003.itcv.features.search.adapter.SearchResultMappers
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.jmlb0003.itcv.features.search.adapter.SearchResult as ResultListItem

@ExperimentalCoroutinesApi
class SearchPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatchers = TestDispatchers(testDispatcher)

    private val viewState = mockk<SearchViewState>(relaxed = true)
    private val navigationTriggers = mockk<NavigationTriggers>(relaxed = true)
    private val resultsMapper = mockk<SearchResultMappers>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val searchUseCase = SearchUseCase(userRepository)
    private val presenter = SearchPresenter(viewState, navigationTriggers, searchUseCase, resultsMapper, dispatchers)

    @Test
    fun `on onSubmitSearch with null query does nothing`() {
        presenter.onSubmitSearch(null)
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers wasNot called
        }
    }

    @Test
    fun `on onSubmitSearch with blank query does nothing`() {
        presenter.onSubmitSearch(" ")
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers wasNot called
        }
    }

    @Test
    fun `on onSubmitSearch with valid query and searchUseCase resulting on error displays an error screen`() =
        testDispatcher.runBlockingTest {
            val query = "some query"
            every { userRepository.searchUserByUsername(query) } returns Either.Left(Failure.NetworkConnection)

            presenter.onSubmitSearch(query)

            verify { viewState.displayErrorScreen() }
        }

    @Test
    fun `on onSubmitSearch with valid query and searchUseCase resulting on success with empty list of results displays a empty screen`() =
        testDispatcher.runBlockingTest {
            val query = "some query"
            every { userRepository.searchUserByUsername(query) } returns Either.Right(emptyList())

            presenter.onSubmitSearch(query)

            verify { viewState.displayEmptyScreen() }
        }

    @Test
    fun `on onSubmitSearch with valid query and searchUseCase resulting on success with results displays the results screen`() =
        testDispatcher.runBlockingTest {
            val query = "some query"
            val results = listOf(mockk<SearchResult>(), mockk())
            every { userRepository.searchUserByUsername(query) } returns Either.Right(results)
            val expectedResults = mockk<List<ResultListItem>>()
            every { resultsMapper.mapToSearchResultListItem(results) } returns expectedResults

            presenter.onSubmitSearch(query)

            verify { viewState.displayResults(expectedResults) }
        }

    @Test
    fun `on onSearchTextChange with null query does nothing`() {
        presenter.onSearchTextChange(null)
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers wasNot called
        }
    }

    @Test
    fun `on onSearchTextChange with blank query does nothing`() {
        presenter.onSearchTextChange(" ")
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers wasNot called
        }
    }

    @Test
    fun `on onSearchTextChange with valid query does nothing`() {
        presenter.onSearchTextChange("a")
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers wasNot called
        }
    }

    @Test
    fun `on onResultClicked with valid query does nothing`() {
        val expectedTitle = "Some title"
        val result = ResultListItem(expectedTitle, false)
        presenter.onResultClicked(result)
        verify {
            viewState wasNot called
            userRepository wasNot called
            navigationTriggers.navigateToProfileDetails(expectedTitle)
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
