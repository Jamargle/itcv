package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    val name: String,
    val description: String,
    val website: String,
    val repoUrl: String
) : Parcelable
