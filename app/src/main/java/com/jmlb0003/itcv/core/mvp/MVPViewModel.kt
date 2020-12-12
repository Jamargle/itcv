package com.jmlb0003.itcv.core.mvp

import androidx.lifecycle.ViewModel

abstract class MVPViewModel<out P : Presenter, out V : Any>(
    val presenter: P,
    val viewState: V? = null
) : ViewModel() {

    init {
        presenter.onStart()
    }

    val nonNullViewState: V = requireNotNull(viewState) { "The view $viewState is null at this point" }

    override fun onCleared() {
        presenter.clear()
        super.onCleared()
    }
}
