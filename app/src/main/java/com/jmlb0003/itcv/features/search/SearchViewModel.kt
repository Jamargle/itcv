package com.jmlb0003.itcv.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel

class SearchViewModel(
    presenter: SearchPresenter,
    viewState: SearchViewState
) : MVPViewModel<SearchPresenter, SearchViewState>(presenter, viewState) {

    class Factory(
        private val injector: SearchInjector
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            SearchViewModel(injector.presenter, injector.viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create SearchViewModel instances")
    }
}

fun getSearchViewModelFactory(scope: SearchFragment) =
    SearchViewModel.Factory(getSearchInjector(scope))
