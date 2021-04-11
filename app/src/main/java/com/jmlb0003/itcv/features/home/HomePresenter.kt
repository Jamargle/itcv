package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase
import com.jmlb0003.itcv.features.MainToolbarController

class HomePresenter(
    private val viewState: HomeViewState,
    private val mainToolbarController: MainToolbarController,
    private val navigationTriggers: NavigationTriggers,
    getUserProfileUseCase: GetUserProfileUseCase,
    dispatchers: Dispatchers
) : Presenter(dispatchers) {

    init {
        getUserProfileUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = GetUserProfileUseCase.Input("Jamargle")
        ) {
            it.either(::handleGetProfileError) { user -> handleGetProfileSuccess(user) }
        }
    }

    private var currentUser: User? = null

    fun onSeeAllClicked() {
        currentUser?.let { navigationTriggers.navigateToProfileDetails(it) }
    }

    fun onUserWebsiteClicked() {
        currentUser?.website?.let { navigationTriggers.openUrl(it) }
    }

    private fun handleGetProfileSuccess(user: User) {
        with(user) {
            currentUser = this
            mainToolbarController.setNewTitle(username)
            displayUserInformation(this)
        }
    }

    private fun handleGetProfileError(result: Failure) {
        when (result) {
            is Failure.NetworkConnection -> viewState.displayErrorMessage(R.string.error_dialog_no_network)
            else -> viewState.displayErrorMessage(R.string.error_dialog_generic_message)
        }
    }

    private fun displayUserInformation(user: User) {
        viewState.displayProfileName(user.name)
        if (user.bio.isNotEmpty()) {
            viewState.displayBio(user.bio)
        } else {
            viewState.hideBio()
        }
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
        viewState.displayRepositoryCount(user.repositoryCount.toString())
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
