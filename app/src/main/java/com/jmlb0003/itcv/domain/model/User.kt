package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class User(
    val username: String,
    val name: String,
    val memberSince: Date,
    val email: String = "",
    val location: String = "",
    val repositoryCount: Int = 0
) : Parcelable
