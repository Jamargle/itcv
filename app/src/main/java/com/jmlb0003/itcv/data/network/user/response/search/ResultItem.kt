package com.jmlb0003.itcv.data.network.user.response.search

import com.google.gson.annotations.SerializedName

data class ResultItem(
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val githubUrl: String
)
