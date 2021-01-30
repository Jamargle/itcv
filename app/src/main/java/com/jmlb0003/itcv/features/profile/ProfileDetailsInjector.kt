package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetProfileDetailsUseCase

class ProfileDetailsInjector(
    getProfileDetailsUseCase: GetProfileDetailsUseCase,
    dispatchers: Dispatchers
) {
    val viewState = ProfileDetailsViewState()
    val presenter = ProfileDetailsPresenter(
        viewState,
        getProfileDetailsUseCase,
        dispatchers
    )
}

fun getProfileDetailsInjector(scope: ProfileDetailsFragment) =
    with(scope.requireActivity()) {
        val getProfileDetailsUseCase = GetProfileDetailsUseCase(
            mainInjector.repositoriesProvider.userRepository,
            mainInjector.repositoriesProvider.repositoriesRepository,
        )
        ProfileDetailsInjector(
            getProfileDetailsUseCase,
            mainInjector.dispatchers
        )
    }
