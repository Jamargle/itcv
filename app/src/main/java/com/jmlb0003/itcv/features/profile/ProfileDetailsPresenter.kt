package com.jmlb0003.itcv.features.profile

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.domain.model.ProfileDetails
import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetProfileDetailsUseCase
import com.jmlb0003.itcv.domain.usecases.GetUserTopicsUseCase
import com.jmlb0003.itcv.features.NavigationTriggers
import com.jmlb0003.itcv.features.profile.adapter.RepositoryMappers.toRepositoryListItem
import com.jmlb0003.itcv.features.profile.adapter.TopicMappers
import com.jmlb0003.itcv.utils.DateExtensions.toShortDateString
import javax.inject.Inject

@FragmentScope
class ProfileDetailsPresenter
@Inject constructor(
    private val viewState: ProfileDetailsViewState,
    private val navigationTriggers: NavigationTriggers,
    private val getProfileDetailsUseCase: GetProfileDetailsUseCase,
    private val getUserTopicsUseCase: GetUserTopicsUseCase,
    private val topicMappers: TopicMappers,
    private val dispatchers: Dispatchers
) : Presenter(dispatchers) {

    fun onUserWebsiteClicked(website: String?) {
        if (!website.isNullOrBlank()) {
            navigationTriggers.openUrl(website)
        }
    }

    fun onRepoGithubUrlClicked(repoUrl: String?) {
        if (!repoUrl.isNullOrBlank()) {
            navigationTriggers.openUrl(repoUrl)
        }
    }

    fun onRepoWebsiteClicked(website: String?) {
        if (!website.isNullOrBlank()) {
            navigationTriggers.openUrl(website)
        }
    }

    fun onViewReady(profileDetailsArguments: ProfileDetailsArgs) {
        viewState.displayLoadingRepos()
        getProfileDetailsUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetProfileDetailsUseCase.Input(
                username = profileDetailsArguments.userName
            )
        ) {
            it.either(::handleGetDetailsError) { profileDetails -> handleGetDetailsSuccess(profileDetails) }
        }

        displayUserTopics(profileDetailsArguments.userName)

        if (profileDetailsArguments.user != null) {
            displayUserInformation(profileDetailsArguments.user)
        }
    }

    private fun handleGetDetailsSuccess(profileDetails: ProfileDetails) {
        displayUserInformation(profileDetails.user)
        viewState.displayReposInformation(profileDetails.repositories.map { it.toRepositoryListItem() })
    }

    private fun handleGetDetailsError(failure: Failure) {
        viewState.hideRepos()
        viewState.displayErrorMessage(failure.message)
    }

    private fun displayUserTopics(userName: String) {
        viewState.displayLoadingTopics()
        getUserTopicsUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetUserTopicsUseCase.Input(
                username = userName
            )
        ) { it.either(::handleGetTopicsError, ::handleGetTopicsSuccess) }
    }

    private fun handleGetTopicsSuccess(topics: List<Topic>) {
        if (topics.isEmpty()) {
            viewState.hideTopics()
        } else {
            val topicsToDisplay = topicMappers.mapToPresentationItems(topics)
            viewState.displayTopics(topicsToDisplay)
        }
    }

    private fun handleGetTopicsError(failure: Failure) {
        viewState.hideTopics()
    }

    private fun displayUserInformation(user: User) {
        viewState.displayProfileName(user.name)
        if (user.bio.isNotEmpty()) {
            viewState.displayBio(user.bio)
        } else {
            viewState.hideBio()
        }
        viewState.displayMemberSince(user.memberSince.toShortDateString())
        if (user.email.isNotEmpty()) {
            viewState.displayEmail(user.email)
        } else {
            viewState.hideEmail()
        }
        if (user.location.isNotEmpty()) {
            viewState.displayLocation(user.location)
        } else {
            viewState.hideLocation()
        }
        viewState.displayFollowerCount(user.followerCount.toString())
        if (user.website.isNotEmpty()) {
            viewState.displayWebsite(user.website)
        } else {
            viewState.hideWebsite()
        }
        if (user.twitterAccount.isNotEmpty()) {
            viewState.displayTwitterAccount(user.twitterAccount)
        } else {
            viewState.hideTwitterAccount()
        }
    }
}
