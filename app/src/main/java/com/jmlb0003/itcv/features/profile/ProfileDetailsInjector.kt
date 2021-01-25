package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector

class ProfileDetailsInjector(
    dispatchers: Dispatchers
) {
    val viewState = ProfileDetailsViewState()
    val presenter = ProfileDetailsPresenter(
        viewState,
        dispatchers
    )
}

fun getProfileDetailsInjector(scope: ProfileDetailsFragment) =
    with(scope.requireActivity()) {
        ProfileDetailsInjector(
            mainInjector.dispatchers
        )
    }
