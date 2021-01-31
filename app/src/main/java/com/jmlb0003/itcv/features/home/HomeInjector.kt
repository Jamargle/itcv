package com.jmlb0003.itcv.features.home

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase

class HomeInjector(
    navigationTriggers: NavigationTriggers,
    getUserProfileUseCase: GetUserProfileUseCase,
    dispatchers: Dispatchers
) {
    val presenter by lazy {
        HomePresenter(
            viewState,
            navigationTriggers,
            getUserProfileUseCase,
            dispatchers
        )
    }
    val viewState = HomeViewState()
}

fun getHomeInjector(scope: HomeFragment) =
    with(scope.requireActivity()) {
        val navigationTriggers = scope.activityViewModels<NavigationTriggers>().value
        val getUserProfileUseCase = GetUserProfileUseCase(mainInjector.repositoriesProvider.userRepository)
        HomeInjector(
            navigationTriggers,
            getUserProfileUseCase,
            mainInjector.dispatchers
        )
    }
