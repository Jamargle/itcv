package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class User(
    val avatarUrl: String,
    val username: String,
    val name: String,
    val bio: String = "",
    val memberSince: Date,
    val email: String = "",
    val location: String = "",
    val repositoryCount: Int = 0,
    val followerCount: Int = 0,
    val website: String = "",
    val twitterAccount: String = ""
) : Parcelable
