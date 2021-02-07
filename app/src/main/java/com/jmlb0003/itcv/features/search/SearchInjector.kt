package com.jmlb0003.itcv.features.search

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.SearchUseCase
import com.jmlb0003.itcv.features.home.NavigationTriggers

class SearchInjector(
    searchUseCase: SearchUseCase,
    navigationTriggers: NavigationTriggers,
    dispatchers: Dispatchers
) {
    val presenter by lazy {
        SearchPresenter(
            viewState,
            navigationTriggers,
            searchUseCase,
            dispatchers
        )
    }
    val viewState = SearchViewState()
}

fun getSearchInjector(scope: SearchFragment) =
    with(scope.requireActivity()) {
        val navigationTriggers = scope.activityViewModels<NavigationTriggers>().value
        val searchUseCase = SearchUseCase(mainInjector.repositoriesProvider.userRepository)
        SearchInjector(
            searchUseCase,
            navigationTriggers,
            mainInjector.dispatchers
        )
    }
