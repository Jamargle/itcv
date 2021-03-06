package com.jmlb0003.itcv.features.profile

import androidx.lifecycle.MutableLiveData
import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.profile.adapter.RepoListItem
import com.jmlb0003.itcv.features.profile.adapter.TopicListItem
import javax.inject.Inject

@FragmentScope
class ProfileDetailsViewState
@Inject constructor() {

    val profileRepositories: MutableLiveData<RepositoriesStateList> = MutableLiveData()
    val profileTopics: MutableLiveData<TopicsStateList> = MutableLiveData()
    val profileNameState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val profileBioState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val memberSinceState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val emailState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val locationState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val followerCountState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val userWebsiteState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()
    val twitterAccountState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()

    val errorState: MutableLiveData<ProfileDetailsStateList> = MutableLiveData()

    fun displayErrorMessage(message: String?) = errorState.postValue(ProfileDetailsStateList.Error(message))

    fun displayProfileName(profileName: String) = profileNameState.postValue(ProfileDetailsStateList.Ready(profileName))
    fun displayMemberSince(memberSince: String) = memberSinceState.postValue(ProfileDetailsStateList.Ready(memberSince))
    fun displayLoadingRepos() = profileRepositories.postValue(RepositoriesStateList.Loading)
    fun hideRepos() = profileRepositories.postValue(RepositoriesStateList.Hidden)
    fun displayReposInformation(repositories: List<RepoListItem>) =
        profileRepositories.postValue(RepositoriesStateList.Ready(repositories))

    fun displayLoadingTopics() = profileTopics.postValue(TopicsStateList.Loading)
    fun hideTopics() = profileTopics.postValue(TopicsStateList.Hidden)
    fun displayTopics(topics: List<TopicListItem>) = profileTopics.postValue(TopicsStateList.Ready(topics))

    fun displayBio(bio: String) = profileBioState.postValue(ProfileDetailsStateList.Ready(bio))
    fun hideBio() = profileBioState.postValue(ProfileDetailsStateList.Hidden)
    fun displayEmail(email: String) = emailState.postValue(ProfileDetailsStateList.Ready(email))
    fun hideEmail() = emailState.postValue(ProfileDetailsStateList.Hidden)
    fun displayLocation(location: String) = locationState.postValue(ProfileDetailsStateList.Ready(location))
    fun hideLocation() = locationState.postValue(ProfileDetailsStateList.Hidden)
    fun displayFollowerCount(followerCount: String) =
        followerCountState.postValue(ProfileDetailsStateList.Ready(followerCount))

    fun displayWebsite(website: String) = userWebsiteState.postValue(ProfileDetailsStateList.Ready(website))
    fun hideWebsite() = userWebsiteState.postValue(ProfileDetailsStateList.Hidden)
    fun displayTwitterAccount(twitterAccount: String) =
        twitterAccountState.postValue(ProfileDetailsStateList.Ready(twitterAccount))

    fun hideTwitterAccount() = twitterAccountState.postValue(ProfileDetailsStateList.Hidden)
}
