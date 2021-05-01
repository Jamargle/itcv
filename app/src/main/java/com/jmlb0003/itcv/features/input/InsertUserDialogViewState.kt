package com.jmlb0003.itcv.features.input

import androidx.lifecycle.MutableLiveData

class InsertUserDialogViewState {

    val viewState = MutableLiveData<InsertUserDialogViewStateList>()
    val doneButtonState = MutableLiveData(false)

    fun displayCurrentUsername(username: String) =
        viewState.postValue(InsertUserDialogViewStateList.Visible(username))

    fun leaveView(newUserName: String? = null) = viewState.postValue(InsertUserDialogViewStateList.Leaving(newUserName))

    fun disableDoneButton() = doneButtonState.postValue(false)

    fun enableDoneButton() = doneButtonState.postValue(true)
}
