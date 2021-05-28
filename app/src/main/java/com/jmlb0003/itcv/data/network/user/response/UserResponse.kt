package com.jmlb0003.itcv.data.network.user.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserResponse(
    @SerializedName("avatar_url") val avatar: String,
    @SerializedName("login") val username: String,
    @SerializedName("name") val name: String?,
    @SerializedName("bio") val bioDescription: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("public_repos") val reposCount: Int,
    @SerializedName("followers") val followerCount: Int,
    @SerializedName("created_at") val profileCreatedDate: Date,
    @SerializedName("blog") val userWebsite: String?,
    @SerializedName("twitter_username") val twitterAccount: String?
)
