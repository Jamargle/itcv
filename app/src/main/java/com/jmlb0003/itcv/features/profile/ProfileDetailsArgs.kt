package com.jmlb0003.itcv.features.profile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDetailsArgs(
    val profileName: String
) : Parcelable
