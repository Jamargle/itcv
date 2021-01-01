package com.jmlb0003.itcv.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel

class SettingsViewModel(
    presenter: SettingsPresenter,
    viewState: SettingsViewState
) : MVPViewModel<SettingsPresenter, SettingsViewState>(presenter, viewState) {

    class Factory(private val injector: SettingsInjector) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            SettingsViewModel(injector.presenter, injector.viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create SettingsViewModel instances")
    }
}

fun getSettingsViewModelFactory(scope: SettingsFragment) =
    SettingsViewModel.Factory(getSettingsInjector(scope))
