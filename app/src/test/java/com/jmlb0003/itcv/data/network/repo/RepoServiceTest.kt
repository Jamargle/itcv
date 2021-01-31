package com.jmlb0003.itcv.data.network.repo

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.mockNetworkConnected
import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class RepoServiceTest {

    private val networkHandler = mockk<NetworkHandler>()
    private val gson = mockk<Gson>()
    private val apiClient = mockk<RepositoryApiClient>()

    private val service = RepoService(apiClient, gson, networkHandler)

    @Test
    fun `on getRepositories with failure from backend returns the failure`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<List<RepoResponse>>>()
        val username = "username"
        every { apiClient.getRepositoriesForUser(username) } returns call
        every { call.execute() } throws RuntimeException("")
        val url = "https://some/url.com"
        val request = Request.Builder().url(url).build()
        every { call.request() } returns request

        val result = service.getRepositories(username)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
    }

    @Test
    fun `on getRepositories with error while parsing json from backend returns NetworkRequestError`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<List<RepoResponse>>>()
        val username = "username"
        every { apiClient.getRepositoriesForUser(username) } returns call
        val response = mockk<Response<List<RepoResponse>>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        every { response.body() } throws JsonParseException("")


        val result = service.getRepositories(username)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
        assertEquals(
            "There was an error parsing response to user's repositories",
            result.leftValue.error?.message
        )
    }

    @Test
    fun `on getRepositories with success from backend returns the user profile`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<List<RepoResponse>>>()
        val username = "username"
        every { apiClient.getRepositoriesForUser(username) } returns call
        val response = mockk<Response<List<RepoResponse>>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        val responseBody = mockk<List<RepoResponse>>()
        every { response.body() } returns responseBody

        val result = service.getRepositories(username)
        assertEquals(responseBody, (result as Either.Right).rightValue)
    }
}
