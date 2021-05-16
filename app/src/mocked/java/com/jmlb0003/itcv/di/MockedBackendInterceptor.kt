package com.jmlb0003.itcv.di

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class MockedBackendInterceptor(
    private val mockedResponsesMapper: MockedResponsesMapper,
    private val responseFileReader: ResponseFileReader
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val endpoint = chain.request().url.toUri().path
        val responseFilePath = mockedResponsesMapper.getResponseFilePathFor(endpoint)
        val responseString = responseFilePath?.let { responseFileReader.read(it) }

        if (responseString.isNullOrBlank()) {
            throw IllegalStateException("Missing response for $endpoint")
        } else {
            return chain.proceed(chain.request())
                .newBuilder()
                .message(responseString)
                .body(responseString.toResponseBody("application/json".toMediaType()))
                .request(chain.request())
                .build()
        }
    }
}
