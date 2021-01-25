package com.jmlb0003.itcv.features.profile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class ProfileDetailsArgs(
    val userName: String,
    val memberSince: Date
) : Parcelable
