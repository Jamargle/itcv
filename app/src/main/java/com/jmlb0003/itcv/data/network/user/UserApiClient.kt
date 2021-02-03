package com.jmlb0003.itcv.data.network.user

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.data.network.user.response.search.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiClient {

    @GET("/users/{username}")
    fun getUserProfile(@Path("username") username: String): Call<UserResponse>

    @GET("/search/users")
    fun searchUserByUsername(@Query("q") username: String): Call<SearchUserResponse>
}
