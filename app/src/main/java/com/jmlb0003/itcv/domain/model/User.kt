package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String,
    val email: String = "",
    val location: String = "",
    val repositoryCount: Int = 0
) : Parcelable
