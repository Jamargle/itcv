package com.jmlb0003.itcv.features.search

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.usecases.SearchUseCase
import com.jmlb0003.itcv.features.home.NavigationTriggers
import com.jmlb0003.itcv.features.search.adapter.SearchResultMappers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.jmlb0003.itcv.features.search.adapter.SearchResult as ResultListItem

class SearchPresenter(
    private val viewState: SearchViewState,
    private val navigationTriggers: NavigationTriggers,
    private val searchUseCase: SearchUseCase,
    private val resultsMapper: SearchResultMappers,
    private val dispatchers: Dispatchers
) : Presenter(dispatchers) {

    fun onSubmitSearch(query: String?) {
        if (!query.isNullOrBlank()) {
            search(query)
        }
    }

    fun onSearchTextChange(newText: String?) {
        // TODO to be implemented for suggestion, history, etc...
    }

    fun onResultClicked(searchResult: ResultListItem) {
        navigationTriggers.navigateToProfileDetails(searchResult.username)
    }

    private fun search(query: String) {
        viewState.displayLoading()
        searchUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = SearchUseCase.Input(
                usernameQuery = query
            )
        ) {
            it.either(::handleSearchError) { results -> handleSearchSuccess(results) }
        }
    }

    private fun handleSearchSuccess(results: List<SearchResult>) {
        if (results.isEmpty()) {
            viewState.displayEmptyScreen()
        } else {
            launch {
                val resultsToShow = mapResultsToPresentation(results)
                viewState.displayResults(resultsToShow)
            }
        }
    }

    private fun handleSearchError(error: Failure) {
        viewState.displayErrorScreen()
    }

    private suspend fun mapResultsToPresentation(results: List<SearchResult>) =
        withContext(dispatchers.backgroundThread) {
            resultsMapper.mapToSearchResultListItem(results)
        }
}
