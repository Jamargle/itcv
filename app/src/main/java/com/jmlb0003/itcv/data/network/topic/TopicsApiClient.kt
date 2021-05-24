package com.jmlb0003.itcv.data.network.topic

import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TopicsApiClient {

    // As long as this endpoint is in preview period, it requires merci-preview-header.
    // More info:https://docs.github.com/rest/reference/repos#get-all-repository-topics
    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("/repos/{owner}/{repo}/topics")
    fun getRepositoryTopics(
        @Path("owner") userName: String,
        @Path("repo") repoName: String
    ): Call<TopicsResponse>
}
