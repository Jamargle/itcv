package com.jmlb0003.itcv.features.home

import androidx.annotation.StringRes

sealed class HomeViewErrorState {
    object ErrorMissingDefaultUser : HomeViewErrorState()
    class ErrorStringRes(@StringRes val errorStringRes: Int) : HomeViewErrorState()
    class ErrorMessage(val errorMessage: String) : HomeViewErrorState()
}
