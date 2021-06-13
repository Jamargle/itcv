package com.jmlb0003.itcv.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel
import com.jmlb0003.itcv.di.FragmentScope
import javax.inject.Inject

class SearchViewModel(
    presenter: SearchPresenter,
    viewState: SearchViewState
) : MVPViewModel<SearchPresenter, SearchViewState>(presenter, viewState) {

    @FragmentScope
    class Factory
    @Inject constructor(
        private val presenter: SearchPresenter,
        private val viewState: SearchViewState
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            SearchViewModel(presenter, viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create SearchViewModel instances")
    }
}
