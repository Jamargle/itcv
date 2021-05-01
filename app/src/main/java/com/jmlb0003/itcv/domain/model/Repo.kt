package com.jmlb0003.itcv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repo(
    val name: String,
    val description: String,
    val website: String,
    val repoUrl: String,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int
) : Parcelable
