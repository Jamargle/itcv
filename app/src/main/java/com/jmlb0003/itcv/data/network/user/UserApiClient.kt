package com.jmlb0003.itcv.data.network.user

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiClient {

    @GET("/users/{username}")
    fun getUserProfile(@Path("username") username: String): Call<UserResponse>
}
