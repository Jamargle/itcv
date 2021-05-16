package com.jmlb0003.itcv.data.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.mockNetworkConnected
import com.jmlb0003.itcv.data.mockNetworkDisconnected
import io.mockk.every
import io.mockk.mockk
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader

class BaseServiceTest {

    private val networkHandler = mockk<NetworkHandler>()
    private val gson = mockk<Gson>()
    private val anyCall = mockk<Call<Int>>()
    private val baseService = ServiceTestImpl(networkHandler, gson)

    @Test
    fun `on performCall with network disconnected returns error NetworkConnection`() {
        networkHandler.mockNetworkDisconnected()

        val result = baseService.testPerformCall(anyCall)

        assertEquals(Failure.NetworkConnection, (result as Either.Left).leftValue)
    }

    @Test
    fun `on performCall with network connected and json parse exception returns error NetworkRequestError`() {
        networkHandler.mockNetworkConnected()
        every { anyCall.execute() } throws JsonParseException("")

        val result = baseService.testPerformCall(anyCall)

        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
        assertEquals("There was an error parsing response", result.leftValue.error?.message)
    }

    @Test
    fun `on performCall with network connected and io exception returns error ServerError`() {
        networkHandler.mockNetworkConnected()
        every { anyCall.execute() } throws IOException("")
        val expectedUrl = "https://some/url.com"
        val request = Request.Builder().url(expectedUrl).build()
        every { anyCall.request() } returns request

        val result = baseService.testPerformCall(anyCall)

        assertTrue((result as Either.Left).leftValue is Failure.ServerError)
        assertEquals("Error talking to the server for the request $expectedUrl", result.leftValue.error?.message)
    }

    @Test
    fun `on performCall with network connected and any other exception returns error NetworkRequestError`() {
        networkHandler.mockNetworkConnected()
        every { anyCall.execute() } throws RuntimeException("")
        val expectedUrl = "https://some/url.com"
        val request = Request.Builder().url(expectedUrl).build()
        every { anyCall.request() } returns request

        val result = baseService.testPerformCall(anyCall)

        assertTrue((result as Either.Left).leftValue is Failure.NetworkRequestError)
        assertEquals("Error while performing the request $expectedUrl", result.leftValue.error?.message)
    }

    @Test
    fun `on performCall with network connected and success response returns the response`() {
        networkHandler.mockNetworkConnected()
        val response = mockk<Response<Int>>()
        every { anyCall.execute() } returns response
        every { response.isSuccessful } returns true

        val result = baseService.testPerformCall(anyCall)

        assertTrue(result.isRight)
    }

    @Test
    fun `on performCall with network connected and error response returns the error message from the error body`() {
        networkHandler.mockNetworkConnected()
        val response = mockk<Response<Int>>()
        every { anyCall.execute() } returns response
        every { response.isSuccessful } returns false
        val errorBody = mockk<ResponseBody>()
        every { response.errorBody() } returns errorBody
        every { errorBody.close() } returns Unit
        val reader = mockk<Reader>()
        every { errorBody.charStream() } returns reader
        val errorResponse = mockk<ResponseFailure>()
        every { gson.fromJson(reader, ResponseFailure::class.java) } returns errorResponse
        val errorMessage = "Some error message"
        every { errorResponse.errorMessage } returns errorMessage

        val result = baseService.testPerformCall(anyCall)

        assertTrue(result.isLeft)
        assertEquals(errorMessage, (result as Either.Left).leftValue.message)
    }

    @Test
    fun `on performCall with network connected and error response without error body returns generic error message`() {
        networkHandler.mockNetworkConnected()
        val response = mockk<Response<Int>>()
        every { anyCall.execute() } returns response
        every { response.isSuccessful } returns false
        every { response.errorBody() } returns null

        val result = baseService.testPerformCall(anyCall)

        assertTrue(result.isLeft)
        assertEquals("Error talking to the server", (result as Either.Left).leftValue.message)
    }

    private class ServiceTestImpl(networkHandler: NetworkHandler, gson: Gson) : BaseService(networkHandler, gson) {
        fun <T> testPerformCall(call: Call<T>) = super.performCall(call)
    }
}
