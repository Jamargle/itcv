package com.jmlb0003.itcv.di

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

private const val RESPONSE_SUCCES_CODE = 200
private const val RESPONSE_ERROR_CODE = 499

class MockedBackendInterceptor(
    private val mockedResponsesMapper: MockedResponsesMapper,
    private val responseFileReader: ResponseFileReader
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val endpoint = chain.request().url.toUri().path
        val responseFilePath = mockedResponsesMapper.getResponseFilePathFor(endpoint)
        val responseString = responseFilePath?.let { responseFileReader.read(it) } ?: ""

        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .apply {
                if (responseString.isBlank()) {
                    code(RESPONSE_ERROR_CODE)
                        .message("Missing response for $endpoint")
                } else {
                    code(RESPONSE_SUCCES_CODE)
                        .message(responseString)
                }
            }
            .body(responseString.toResponseBody("application/json".toMediaType()))
            .build()
    }
}
