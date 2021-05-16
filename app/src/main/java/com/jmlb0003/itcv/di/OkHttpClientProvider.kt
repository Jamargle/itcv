package com.jmlb0003.itcv.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpClientProvider(
    private val interceptors: List<Interceptor>
) {

    val okHttpClient
        get() = OkHttpClient.Builder()
            .apply {
                interceptors.forEach { addInterceptor(it) }
            }
            .build()
}
