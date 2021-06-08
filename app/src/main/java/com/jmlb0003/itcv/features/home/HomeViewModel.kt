package com.jmlb0003.itcv.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel
import com.jmlb0003.itcv.di.FragmentScope
import javax.inject.Inject

class HomeViewModel(
    presenter: HomePresenter,
    viewState: HomeViewState
) : MVPViewModel<HomePresenter, HomeViewState>(presenter, viewState) {

    @FragmentScope
    class Factory
    @Inject constructor(
        private val presenter: HomePresenter,
        private val viewState: HomeViewState
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            HomeViewModel(presenter, viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create HomeViewModel instances")
    }
}
