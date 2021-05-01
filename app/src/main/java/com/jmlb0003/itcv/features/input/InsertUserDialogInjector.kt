package com.jmlb0003.itcv.features.input

import androidx.fragment.app.Fragment
import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.di.mainInjector
import com.jmlb0003.itcv.domain.usecases.GetDefaultUserProfileUseCase
import com.jmlb0003.itcv.domain.usecases.UpdateDefaultUserUseCase

class InsertUserDialogInjector(
    private val getDefaultUserProfileUseCase: GetDefaultUserProfileUseCase,
    private val updateDefaultUserUseCase: UpdateDefaultUserUseCase,
    private val dispatchers: Dispatchers
) {
    val viewState = InsertUserDialogViewState()

    val presenter by lazy {
        InsertUserDialogPresenter(
            viewState,
            getDefaultUserProfileUseCase,
            updateDefaultUserUseCase,
            dispatchers
        )
    }
}

fun getInsertUserDialogInjector(scope: Fragment) =
    with(scope.requireActivity().mainInjector) {
        val getDefaultUserProfileUseCase = GetDefaultUserProfileUseCase(repositoriesProvider.userRepository)
        val updateDefaultUserUseCase = UpdateDefaultUserUseCase(repositoriesProvider.userRepository)
        InsertUserDialogInjector(
            getDefaultUserProfileUseCase,
            updateDefaultUserUseCase,
            dispatchers
        )
    }
