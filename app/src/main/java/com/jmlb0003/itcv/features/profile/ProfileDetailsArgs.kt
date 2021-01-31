package com.jmlb0003.itcv.features.profile

import android.os.Parcelable
import com.jmlb0003.itcv.domain.model.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDetailsArgs(
    val user: User? = null,
    val userName: String = user?.username ?: throw IllegalArgumentException("Please provide a user or a username")
) : Parcelable
