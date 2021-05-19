package com.jmlb0003.itcv.features.profile

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetProfileDetailsUseCase
import com.jmlb0003.itcv.domain.usecases.GetUserTopicsUseCase
import com.jmlb0003.itcv.features.home.NavigationTriggers
import com.jmlb0003.itcv.features.profile.adapter.TopicMappers

class ProfileDetailsInjector(
    navigationTriggers: NavigationTriggers,
    getProfileDetailsUseCase: GetProfileDetailsUseCase,
    getUserTopicsUseCase: GetUserTopicsUseCase,
    topicMappers: TopicMappers,
    dispatchers: Dispatchers
) {
    val viewState = ProfileDetailsViewState()
    val presenter = ProfileDetailsPresenter(
        viewState,
        navigationTriggers,
        getProfileDetailsUseCase,
        getUserTopicsUseCase,
        topicMappers,
        dispatchers
    )
}

fun getProfileDetailsInjector(scope: ProfileDetailsFragment) =
    with(scope.requireActivity()) {
        val navigationTriggers = scope.activityViewModels<NavigationTriggers>().value
        val getProfileDetailsUseCase = GetProfileDetailsUseCase(
            mainInjector.repositoriesProvider.userRepository,
            mainInjector.repositoriesProvider.repositoriesRepository,
        )
        val getUserTopicsUseCase = GetUserTopicsUseCase(
            mainInjector.repositoriesProvider.repositoriesRepository,
            mainInjector.repositoriesProvider.topicsRepository,
        )
        ProfileDetailsInjector(
            navigationTriggers,
            getProfileDetailsUseCase,
            getUserTopicsUseCase,
            TopicMappers,
            mainInjector.dispatchers
        )
    }
