package com.jmlb0003.itcv.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel

class ProfileDetailsViewModel(
    presenter: ProfileDetailsPresenter,
    viewState: ProfileDetailsViewState
) : MVPViewModel<ProfileDetailsPresenter, ProfileDetailsViewState>(presenter, viewState) {

    class Factory(
        private val injector: ProfileDetailsInjector
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            ProfileDetailsViewModel(injector.presenter, injector.viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create ProfileDetailsViewModel instances")
    }
}

fun ProfileDetailsFragment.getProfileDetailsViewModelFactory() =
    ProfileDetailsViewModel.Factory(getProfileDetailsInjector(this))
