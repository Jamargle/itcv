package com.jmlb0003.itcv.features.settings

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.features.settings.adapter.SettingsItem

class SettingsPresenter(
    private val viewState: SettingsViewState,
    private val settingsNavigationTriggers: SettingsNavigationTriggers,
    dispatchers: Dispatchers
) : Presenter(dispatchers) {

    fun onViewPrepared() {
        viewState.displaySettings(
            listOf(
                SettingsItem.About
            )
        )
    }

    fun onSettingsItemClicked(item: SettingsItem) {
        when (item) {
            SettingsItem.About -> settingsNavigationTriggers.navigateToAboutScreen()
        }
    }
}
