package com.jmlb0003.itcv.data.network.repo.response

import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("homepage") val websiteUrl: String?,
    @SerializedName("html_url") val repoUrl: String?,
    @SerializedName("stargazers_count") val starsCount: Int = 0,
    @SerializedName("forks") val forksCount: Int = 0,
    @SerializedName("watchers_count") val watchersCount: Int = 0
)
