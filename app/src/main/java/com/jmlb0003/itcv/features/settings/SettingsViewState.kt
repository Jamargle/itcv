package com.jmlb0003.itcv.features.settings

import androidx.lifecycle.MutableLiveData
import com.jmlb0003.itcv.features.settings.adapter.SettingsItem

class SettingsViewState {

    val settingsViewState: MutableLiveData<List<SettingsItem>> = MutableLiveData()

    fun displaySettings(settings: List<SettingsItem>) =
        settingsViewState.postValue(settings)
}
