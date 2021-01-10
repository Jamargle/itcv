package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase

class HomeInjector(
    getUserProfileUseCase: GetUserProfileUseCase,
    dispatchers: Dispatchers
) {
    val presenter by lazy {
        HomePresenter(
            viewState,
            getUserProfileUseCase,
            dispatchers
        )
    }
    val viewState = HomeViewState()
}

fun getHomeInjector(scope: HomeFragment) =
    with(scope.requireActivity()) {
        val getUserProfileUseCase = GetUserProfileUseCase(mainInjector.repositoriesProvider.userRepository)
        HomeInjector(
            getUserProfileUseCase,
            mainInjector.dispatchers
        )
    }
