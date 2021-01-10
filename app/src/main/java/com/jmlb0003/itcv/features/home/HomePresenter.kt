package com.jmlb0003.itcv.features.home

import com.jmlb0003.itcv.R
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetUserProfileUseCase

class HomePresenter(
    private val viewState: HomeViewState,
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

    private fun handleGetProfileSuccess(user: User) {
        viewState.displayUserInfo(user)
    }

    private fun handleGetProfileError(result: Failure) {
        when (result) {
            is Failure.NetworkConnection -> viewState.displayErrorMessage(R.string.error_dialog_no_network)
            else -> viewState.displayErrorMessage(R.string.error_dialog_generic_message)
        }
    }
}
