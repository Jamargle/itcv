package com.jmlb0003.itcv.data.network.topic

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.mockNetworkConnected
import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class TopicsServiceTest {

    private val topicsApiClient = mockk<TopicsApiClient>()
    private val networkHandler = mockk<NetworkHandler>()
    private val gson = mockk<Gson>()
    private val service = TopicsService(topicsApiClient, networkHandler, gson)

    @Test
    fun `on getTopics with failure from backend returns the failure`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<TopicsResponse>>()
        val username = "username"
        val repoName = "repository name"
        every { topicsApiClient.getRepositoryTopics(username, repoName) } returns call
        every { call.execute() } throws RuntimeException("")
        val url = "https://some/url.com"
        val request = Request.Builder().url(url).build()
        every { call.request() } returns request

        val result = service.getTopics(username, repoName)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
    }

    @Test
    fun `on getTopics with error while parsing json from backend returns NetworkRequestError`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<TopicsResponse>>()
        val username = "username"
        val repoName = "repository name"
        every { topicsApiClient.getRepositoryTopics(username, repoName) } returns call
        val response = mockk<Response<TopicsResponse>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        every { response.body() } throws JsonParseException("")


        val result = service.getTopics(username, repoName)
        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
        assertEquals(
            "There was an error parsing response for topics",
            result.leftValue.error?.message
        )
    }

    @Test
    fun `on getTopics with success from backend returns the user profile`() {
        networkHandler.mockNetworkConnected()
        val call = mockk<Call<TopicsResponse>>()
        val username = "username"
        val repoName = "repository name"
        every { topicsApiClient.getRepositoryTopics(username, repoName) } returns call
        val response = mockk<Response<TopicsResponse>>()
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        val responseBody = mockk<TopicsResponse>()
        every { response.body() } returns responseBody

        val result = service.getTopics(username, repoName)
        assertEquals(responseBody, (result as Either.Right).rightValue)
    }
}