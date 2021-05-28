package com.jmlb0003.itcv.di

import okhttp3.Interceptor
import okhttp3.OkHttpClient

class OkHttpClientProvider(
    private val interceptors: List<Interceptor>,
    private val glideInterceptors: List<Interceptor>
) {

    val glideOkHttpClient by lazy {
        okHttpClient.newBuilder()
            .apply {
                interceptors().clear()
                glideInterceptors.forEach { addInterceptor(it) }
            }
            .build()
    }

    val okHttpClient
        get() = OkHttpClient.Builder()
            .apply {
                interceptors.forEach { addInterceptor(it) }
            }
            .build()
}
