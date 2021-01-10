package com.jmlb0003.itcv.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel

class HomeViewModel(
    presenter: HomePresenter,
    viewState: HomeViewState
) : MVPViewModel<HomePresenter, HomeViewState>(presenter, viewState) {

    class Factory(
        private val injector: HomeInjector
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            HomeViewModel(injector.presenter, injector.viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create HomeViewModel instances")
    }
}

fun getHomeViewModelFactory(scope: HomeFragment) =
    HomeViewModel.Factory(getHomeInjector(scope))
