package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileDetails(
    val user: User,
    val repositories: List<Repo>
) : Parcelable
