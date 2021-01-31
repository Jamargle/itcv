package com.jmlb0003.itcv.data.network.repo

import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryApiClient {

    @GET("/users/{username}/repos")
    fun getRepositoriesForUser(@Path("username") username: String): Call<List<RepoResponse>>
}
