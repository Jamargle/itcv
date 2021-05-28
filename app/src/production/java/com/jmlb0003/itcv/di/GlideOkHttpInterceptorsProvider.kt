package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class GlideOkHttpInterceptorsProvider(private val mainInjector: MainInjector) : InterceptorsProvider {

    override fun getInterceptors(): List<Interceptor> =
        mutableListOf<Interceptor>().apply {
            getLoggingInterceptorIfDebug()?.let { add(it) }
        }

    private fun getLoggingInterceptorIfDebug() =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            null
        }
}

fun getGlideInterceptorProvider(mainInjector: MainInjector) = GlideOkHttpInterceptorsProvider(mainInjector)
