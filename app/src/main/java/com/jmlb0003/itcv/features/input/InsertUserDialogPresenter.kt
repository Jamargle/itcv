package com.jmlb0003.itcv.features.input

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.core.mvp.Presenter
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.domain.usecases.UpdateDefaultUserUseCase

class InsertUserDialogPresenter(
    private val viewState: InsertUserDialogViewState,
    private val getDefaultUserProfileUseCase: GetDefaultUserProfileUseCase,
    private val updateDefaultUserUseCase: UpdateDefaultUserUseCase,
    private val dispatchers: Dispatchers
) : Presenter(dispatchers) {

    private var currentUsername = ""

    fun onViewPrepared() {
        getDefaultUserProfileUseCase(
            coroutineScope = this,
            dispatchers = dispatchers,
            params = UseCase.None
        ) {
            it.either(::handleGetProfileError) { user -> handleGetProfileSuccess(user) }
        }
    }

    fun onTextChange(newText: String?) {
        if (newText == currentUsername || newText.isNullOrBlank()) {
            viewState.disableDoneButton()
        } else {
            viewState.enableDoneButton()
        }
    }

    fun onDoneClicked(newUsername: String?) {
        viewState.disableDoneButton()
        if (newUsername != currentUsername && !newUsername.isNullOrBlank()) {
            updateDefaultUserUseCase(
                coroutineScope = this,
                dispatchers = dispatchers,
                params = UpdateDefaultUserUseCase.Input(newUsername)
            ) {
                it.either(::handleUpdateUsernameError) { handleUpdateUsernameSuccess(it) }
            }
        } else {
            viewState.enableDoneButton()
        }
    }

    private fun handleGetProfileError(result: Failure) {
        viewState.displayCurrentUsername("")
    }

    private fun handleGetProfileSuccess(user: User) {
        currentUsername = user.username
        viewState.displayCurrentUsername(currentUsername)
    }

    private fun handleUpdateUsernameError(result: Failure) {
        viewState.leaveView()
    }

    private fun handleUpdateUsernameSuccess(newUsername: String) {
        currentUsername = newUsername
        viewState.leaveView(currentUsername)
    }
}
