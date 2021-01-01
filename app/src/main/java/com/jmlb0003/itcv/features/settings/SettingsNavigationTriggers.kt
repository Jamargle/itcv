package com.jmlb0003.itcv.features.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmlb0003.itcv.core.livedata.Consumable

class SettingsNavigationTriggers(
    private val goToAboutScreenTrigger: MutableLiveData<Consumable<Unit>> = MutableLiveData()
) : ViewModel() {

    fun getAboutScreenNavigationTrigger(): LiveData<Consumable<Unit>> = goToAboutScreenTrigger

    fun navigateToAboutScreen() = goToAboutScreenTrigger.postValue(Consumable(Unit))
}
