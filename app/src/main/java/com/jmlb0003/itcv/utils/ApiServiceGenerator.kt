package com.jmlb0003.itcv.utils

import com.jmlb0003.itcv.di.OkHttpClientProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiServiceGenerator
@Inject constructor(
    private val okHttpClientProvider: OkHttpClientProvider,
    gsonConverterFactory: GsonConverterFactory
) {

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = retrofitBuilder
            .client(okHttpClientProvider.okHttpClient)
            .build()
        return retrofit.create(serviceClass)
    }

    companion object {
        private const val BASE_URL = "https://api.github.com"
    }
}
