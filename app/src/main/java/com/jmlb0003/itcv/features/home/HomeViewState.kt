package com.jmlb0003.itcv.features.home

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.jmlb0003.itcv.domain.model.User

class HomeViewState {

    val viewState: MutableLiveData<HomeViewStateList> = MutableLiveData()

    fun displayUserInfo(user: User) =
        viewState.postValue(HomeViewStateList.Ready(user))

    fun displayErrorMessage(@StringRes errorStringResource: Int) =
        viewState.postValue(HomeViewStateList.Error(errorStringResource))
}
