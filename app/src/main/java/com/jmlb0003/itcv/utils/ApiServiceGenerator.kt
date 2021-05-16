package com.jmlb0003.itcv.utils

import com.jmlb0003.itcv.di.OkHttpClientProvider
import retrofit2.Retrofit

class ApiServiceGenerator(
    private val okHttpClientProvider: OkHttpClientProvider,
    gsonConverterProvider: GsonUtils
) {

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterProvider.gsonConverter)

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
