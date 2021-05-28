package com.jmlb0003.itcv.di

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

private const val RESPONSE_SUCCES_CODE = 200
private const val RESPONSE_ERROR_CODE = 499

class MockGlideOkHttpInterceptorsProvider(private val mainInjector: MainInjector) : InterceptorsProvider {

    override fun getInterceptors(): List<Interceptor> =
        mutableListOf<Interceptor>().apply {
            add(getMockedBackendInterceptor(mainInjector.applicationContext))
        }

    private fun getMockedBackendInterceptor(applicationContext: Context) =
        Interceptor { chain ->
            val endpoint = chain.request().url.toUri().path

            val responseString = ""
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .apply {
                    val iconName = when {
                        // TODO Implement this properly -> See example here https://github.com/TWiStErRob/glide-support/commit/e10619ef737d3adc60e19bf5fb224dc4c0d54055
                        endpoint.startsWith("/u/11638357") -> "ic_github"
                        else -> ""
                    }
                    if (iconName.isEmpty()) {
                        code(RESPONSE_ERROR_CODE)
                            .message("Missing response for $endpoint")
                    } else {
                        code(RESPONSE_SUCCES_CODE)
                            .message(responseString)
                    }
                }
                .body(responseString.toResponseBody("image/png".toMediaType()))
                .build()
        }
}

fun getGlideInterceptorProvider(mainInjector: MainInjector) = MockGlideOkHttpInterceptorsProvider(mainInjector)
