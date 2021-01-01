package com.jmlb0003.itcv.features.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.coroutines.DispatchersImp

class SettingsInjector(
    private val settingsNavigationTriggers: SettingsNavigationTriggers,
    private val dispatchers: Dispatchers
) {
    val viewState = SettingsViewState()

    val presenter by lazy {
        SettingsPresenter(
            viewState,
            settingsNavigationTriggers,
            dispatchers
        )
    }
}

fun getSettingsInjector(scope: Fragment): SettingsInjector {
    val dispatchers = DispatchersImp
    val settingsNavigationTriggers = scope.viewModels<SettingsNavigationTriggers>().value
    return SettingsInjector(settingsNavigationTriggers, dispatchers)
}
