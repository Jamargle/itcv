package com.jmlb0003.itcv.features.profile

sealed class ProfileDetailsStateList {
    class Ready(val value: String) : ProfileDetailsStateList()
    object Hidden : ProfileDetailsStateList()
    class Error(val errorMessage: String?) : ProfileDetailsStateList()
}
