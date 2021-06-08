package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockOkHttpInterceptorsProvider
@Inject constructor(
    private val applicationContext: Context
) : InterceptorsProvider {

    override fun getInterceptors(): List<Interceptor> =
        mutableListOf<Interceptor>().apply {
            getLoggingInterceptorIfDebug()?.let { add(it) }
            add(getMockedBackendInterceptor(applicationContext))
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

fun getInterceptorProvider(applicationContext: Context) = MockOkHttpInterceptorsProvider(applicationContext)
