package com.jmlb0003.itcv.features.home

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData

class HomeViewState {

    val profileNameState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val profileBioState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val emailState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val locationState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val repositoryCountState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val followerCountState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val userWebsiteState: MutableLiveData<HomeViewStateList> = MutableLiveData()
    val twitterAccountState: MutableLiveData<HomeViewStateList> = MutableLiveData()

    val errorState: MutableLiveData<HomeViewErrorState> = MutableLiveData()

    fun displayErrorMessage(@StringRes errorStringResource: Int) =
        errorState.postValue(HomeViewErrorState.ErrorStringRes(errorStringResource))

    fun displayProfileName(profileName: String) = profileNameState.postValue(HomeViewStateList.Ready(profileName))

    fun displayBio(bio: String) = profileBioState.postValue(HomeViewStateList.Ready(bio))
    fun hideBio() = profileBioState.postValue(HomeViewStateList.Hidden)

    fun displayEmail(email: String) = emailState.postValue(HomeViewStateList.Ready(email))
    fun hideEmail() = emailState.postValue(HomeViewStateList.Hidden)

    fun displayLocation(location: String) = locationState.postValue(HomeViewStateList.Ready(location))
    fun hideLocation() = locationState.postValue(HomeViewStateList.Hidden)

    fun displayRepositoryCount(repositoryCount: String) =
        repositoryCountState.postValue(HomeViewStateList.Ready(repositoryCount))

    fun displayFollowerCount(followerCount: String) =
        followerCountState.postValue(HomeViewStateList.Ready(followerCount))

    fun displayWebsite(website: String) = userWebsiteState.postValue(HomeViewStateList.Ready(website))
    fun hideWebsite() = userWebsiteState.postValue(HomeViewStateList.Hidden)

    fun displayTwitterAccount(twitterAccount: String) =
        twitterAccountState.postValue(HomeViewStateList.Ready(twitterAccount))

    fun hideTwitterAccount() = twitterAccountState.postValue(HomeViewStateList.Hidden)
}
