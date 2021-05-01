package com.jmlb0003.itcv.features.input

sealed class InsertUserDialogViewStateList {
    data class Visible(val username: String) : InsertUserDialogViewStateList()
    class Leaving(val newUsername: String?) : InsertUserDialogViewStateList()
}
