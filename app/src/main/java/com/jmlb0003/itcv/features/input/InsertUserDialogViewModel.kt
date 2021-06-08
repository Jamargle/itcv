package com.jmlb0003.itcv.features.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmlb0003.itcv.core.mvp.MVPViewModel
import com.jmlb0003.itcv.di.FragmentScope
import javax.inject.Inject

class InsertUserDialogViewModel(
    presenter: InsertUserDialogPresenter,
    viewState: InsertUserDialogViewState
) : MVPViewModel<InsertUserDialogPresenter, InsertUserDialogViewState>(presenter, viewState) {

    @FragmentScope
    class Factory
    @Inject constructor(
        private val presenter: InsertUserDialogPresenter,
        private val viewState: InsertUserDialogViewState
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            InsertUserDialogViewModel(presenter, viewState)
                    as? T
                ?: throw IllegalArgumentException("This factory can only create InsertUserDialogViewModel instances")
    }
}
