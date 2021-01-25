package com.jmlb0003.itcv.features.profile

import androidx.lifecycle.MutableLiveData

class ProfileDetailsViewState {

    val profileNameState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val memberSinceState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()

    fun displayProfileName(profileName: String) = profileNameState.postValue(ProfileDetailsStateList.Ready(profileName))
    fun displayMemberSince(memberSince: String) = memberSinceState.postValue(ProfileDetailsStateList.Ready(memberSince))
}
