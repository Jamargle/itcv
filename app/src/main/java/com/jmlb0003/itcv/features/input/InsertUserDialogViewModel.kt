package com.jmlb0003.itcv.features.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel

class InsertUserDialogViewModel(
    presenter: InsertUserDialogPresenter,
    viewState: InsertUserDialogViewState
) : MVPViewModel<InsertUserDialogPresenter, InsertUserDialogViewState>(presenter, viewState) {

    class Factory(private val injector: InsertUserDialogInjector) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            InsertUserDialogViewModel(injector.presenter, injector.viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create InsertUserDialogViewModel instances")
    }
}

fun getInsertUserDialogViewModelFactory(scope: InsertUserDialog) =
    InsertUserDialogViewModel.Factory(getInsertUserDialogInjector(scope))
