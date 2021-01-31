package com.jmlb0003.itcv.data.network.user

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.mockNetworkConnected
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class UserServiceTest {

    private val networkHandler = mockk<NetworkHandler>()
    private val apiClient = mockk<UserApiClient>()
    private val gson = mockk<Gson>()

    private val service = UserService(apiClient, gson, networkHandler)

    @Test
    fun `on getUserProfile with failure from backend returns the failure`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<UserResponse>>()
        val username = "username"
        every { apiClient.getUserProfile(username) } returns call
        every { call.execute() } throws RuntimeException("")
        val url = "https://some/url.com"
        val request = Request.Builder().url(url).build()
        every { call.request() } returns request

        val result = service.getUserProfile(username)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
    }

    @Test
    fun `on getUserProfile with error while parsing json from backend returns NetworkRequestError`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<UserResponse>>()
        val username = "username"
        every { apiClient.getUserProfile(username) } returns call
        val response = mockk<Response<UserResponse>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        every { response.body() } throws JsonParseException("")


        val result = service.getUserProfile(username)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
        assertEquals(
            "There was an error parsing response to fetch user's profile",
            result.leftValue.error?.message
        )
    }

    @Test
    fun `on getUserProfile with success from backend returns the user profile`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<UserResponse>>()
        val username = "username"
        every { apiClient.getUserProfile(username) } returns call
        val response = mockk<Response<UserResponse>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        val responseBody = mockk<UserResponse>()
        every { response.body() } returns responseBody

        val result = service.getUserProfile(username)
        assertEquals(responseBody, (result as Either.Right).rightValue)
    }
}
