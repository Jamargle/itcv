package com.jmlb0003.itcv.utils

import com.google.gson.GsonBuilder
import com.jmlb0003.itcv.BuildConfig
import com.jmlb0003.itcv.utils.DateExtensions.BACKEND_DATE_FORMAT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceGenerator {

    private const val BASE_URL = "https://api.github.com"

    private val gson by lazy {
        GsonBuilder()
            .setDateFormat(BACKEND_DATE_FORMAT)
            .create()
    }

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient
        get() = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }.build()

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = retrofitBuilder
            .client(okHttpClient)
            .build()
        return retrofit.create(serviceClass)
    }
}
