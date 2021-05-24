package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.data.MissingDefaultUserNameFailure
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.features.MainToolbarController

class HomePresenter(
    private val viewState: HomeViewState,
    private val mainToolbarController: MainToolbarController,
    private val navigationTriggers: NavigationTriggers,
    private val getDefaultUserProfileUseCase: GetDefaultUserProfileUseCase,
    private val dispatchers: Dispatchers
) : Presenter(dispatchers) {

    init {
        getDefaultUserProfile()
    }

    private var currentUser: User? = null

    fun onViewCreated() {
        currentUser?.let { mainToolbarController.setNewTitle(it.username) }
    }

    fun onDefaultUsernameChange() {
        getDefaultUserProfile()
    }

    fun onSeeAllClicked() {
        currentUser?.let { navigationTriggers.navigateToProfileDetails(it) }
    }

    fun onUserWebsiteClicked() {
        currentUser?.website?.let { navigationTriggers.openUrl(it) }
    }

    private fun getDefaultUserProfile() {
        viewState.displayLoading()
        getDefaultUserProfileUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = UseCase.None
        ) {
            it.either(::handleGetProfileError, ::handleGetProfileSuccess)
        }
    }

    private fun handleGetProfileSuccess(user: User) {
        viewState.hideLoading()
        with(user) {
            currentUser = this
            mainToolbarController.setNewTitle(username)
            displayUserInformation(this)
        }
    }

    private fun handleGetProfileError(result: Failure) {
        viewState.hideLoading()
        when (result) {
            is Failure.NetworkConnection -> viewState.displayErrorMessage(R.string.error_dialog_no_network)
            is MissingDefaultUserNameFailure -> viewState.displayEnterUsernameDialog()
            else -> viewState.displayErrorMessage(R.string.error_dialog_generic_message)
        }
    }

    private fun displayUserInformation(user: User) {
        if (user.avatarUrl.isNotEmpty()) {
            viewState.displayProfileAvatar(user.avatarUrl)
        }
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
