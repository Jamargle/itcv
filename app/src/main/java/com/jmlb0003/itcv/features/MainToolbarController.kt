package com.jmlb0003.itcv.features

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainToolbarController(
    private val titleChangeTrigger: MutableLiveData<String> = MutableLiveData(),
    private val titleResChangeTrigger: MutableLiveData<Int> = MutableLiveData()
) : ViewModel() {

    fun getTitleChangeTrigger(): LiveData<String> = titleChangeTrigger
    fun getTitleResChangeTrigger(): LiveData<Int> = titleResChangeTrigger

    fun setNewTitle(newTitle: String) = titleChangeTrigger.postValue(newTitle)

    fun setNewTitle(@StringRes newTitle: Int) = titleResChangeTrigger.postValue(newTitle)
}
