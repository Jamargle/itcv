package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

class MockOkHttpInterceptorsProvider(private val mainInjector: MainInjector) : InterceptorsProvider {

    override fun getInterceptors(): List<Interceptor> =
        mutableListOf<Interceptor>().apply {
            getLoggingInterceptorIfDebug()?.let { add(it) }
            add(getMockedBackendInterceptor(mainInjector.applicationContext))
        }

    private fun getLoggingInterceptorIfDebug() =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            null
        }

    private fun getMockedBackendInterceptor(applicationContext: Context) =
        MockedBackendInterceptor(
            MockedResponsesMapper,
            ResponseFileReader(applicationContext)
        )
}

fun getInterceptorProvider(mainInjector: MainInjector) = MockOkHttpInterceptorsProvider(mainInjector)
