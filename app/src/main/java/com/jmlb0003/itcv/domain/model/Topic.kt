package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val name: String
) : Parcelable
