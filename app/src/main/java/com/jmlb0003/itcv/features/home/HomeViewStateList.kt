package com.jmlb0003.itcv.features.home

import androidx.annotation.StringRes
import com.jmlb0003.itcv.domain.model.User

sealed class HomeViewStateList {
    class Ready(val user: User) : HomeViewStateList()
    class Error(@StringRes val errorStringRes: Int) : HomeViewStateList()
}
