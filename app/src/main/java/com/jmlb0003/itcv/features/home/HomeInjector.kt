package com.jmlb0003.itcv.features.home

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.features.MainToolbarController

class HomeInjector(
    mainToolbarController: MainToolbarController,
    navigationTriggers: NavigationTriggers,
    getUserProfileUseCase: GetDefaultUserProfileUseCase,
    dispatchers: Dispatchers
) {
    val presenter by lazy {
        HomePresenter(
            viewState,
            mainToolbarController,
            navigationTriggers,
            getUserProfileUseCase,
            dispatchers
        )
    }
    val viewState = HomeViewState()
}

fun getHomeInjector(scope: HomeFragment) =
    with(scope.requireActivity()) {
        val mainToolbarController = scope.activityViewModels<MainToolbarController>().value
        val navigationTriggers = scope.activityViewModels<NavigationTriggers>().value
        val getUserProfileUseCase = GetDefaultUserProfileUseCase(mainInjector.repositoriesProvider.userRepository)
        HomeInjector(
            mainToolbarController,
            navigationTriggers,
            getUserProfileUseCase,
            mainInjector.dispatchers
        )
    }
