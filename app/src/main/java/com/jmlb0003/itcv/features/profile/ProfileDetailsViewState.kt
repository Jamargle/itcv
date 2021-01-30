package com.jmlb0003.itcv.features.profile

import androidx.lifecycle.MutableLiveData
import com.jmlb0003.itcv.features.profile.adapter.RepoListItem

class ProfileDetailsViewState {

    val profileRepositories: MutableLiveData<List<RepoListItem>> = MutableLiveData()
    val profileNameState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val memberSinceState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()

    fun displayProfileName(profileName: String) = profileNameState.postValue(ProfileDetailsStateList.Ready(profileName))
    fun displayMemberSince(memberSince: String) = memberSinceState.postValue(ProfileDetailsStateList.Ready(memberSince))
    fun displayReposInformation(repositories: List<RepoListItem>) = profileRepositories.postValue(repositories)
}
