package com.jmlb0003.itcv.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jmlb0003.itcv.core.livedata.Consumable
import com.jmlb0003.itcv.domain.model.User

class NavigationTriggers(
    private val goToProfileDetails: MutableLiveData<Consumable<User>> = MutableLiveData(),
    private val goToProfileDetailsByUsername: MutableLiveData<Consumable<String>> = MutableLiveData(),
    private val openWebUrl: MutableLiveData<Consumable<String>> = MutableLiveData()
) : ViewModel() {

    fun getGoToProfileDetailsTrigger(): LiveData<Consumable<User>> = goToProfileDetails
    fun getGoToProfileDetailsByUsernameTrigger(): LiveData<Consumable<String>> = goToProfileDetailsByUsername
    fun getOpenUrlTrigger(): LiveData<Consumable<String>> = openWebUrl

    fun navigateToProfileDetails(profile: User) = goToProfileDetails.postValue(Consumable(profile))

    fun navigateToProfileDetails(username: String) = goToProfileDetailsByUsername.postValue(Consumable(username))

    fun openUrl(website: String) = openWebUrl.postValue(Consumable(website))
}
