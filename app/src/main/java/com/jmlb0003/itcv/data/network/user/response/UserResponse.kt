package com.jmlb0003.itcv.data.network.user.response

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserResponse(
    @SerializedName("login") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("public_repos") val reposCount: Int = 0,
    @SerializedName("followers") val followerCount: Int = 0,
    @SerializedName("following") val followingCount: Int = 0,
    @SerializedName("created_at") val profileCreatedDate: Date
)
