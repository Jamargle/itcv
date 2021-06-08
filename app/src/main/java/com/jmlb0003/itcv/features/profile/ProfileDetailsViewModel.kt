package com.jmlb0003.itcv.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel
import com.jmlb0003.itcv.di.FragmentScope
import javax.inject.Inject

class ProfileDetailsViewModel(
    presenter: ProfileDetailsPresenter,
    viewState: ProfileDetailsViewState
) : MVPViewModel<ProfileDetailsPresenter, ProfileDetailsViewState>(presenter, viewState) {

    @FragmentScope
    class Factory
    @Inject constructor(
        private val presenter: ProfileDetailsPresenter,
        private val viewState: ProfileDetailsViewState
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            ProfileDetailsViewModel(presenter, viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create ProfileDetailsViewModel instances")
    }
}
