package com.jmlb0003.itcv

import android.app.Application
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.coroutines.DispatchersImp
import com.jmlb0003.itcv.di.MainInjector
import com.jmlb0003.itcv.di.OkHttpClientProvider
import com.jmlb0003.itcv.di.RepositoriesProvider
import com.jmlb0003.itcv.di.getGlideInterceptorProvider
import com.jmlb0003.itcv.di.getInterceptorProvider

class CustomApplication : Application() {

    val mainInjector: MainInjector by lazy { createMainInjector() }

    private val okHttpInterceptorsProvider by lazy {
        getInterceptorProvider(mainInjector)
    }

    private val glideInterceptorsProvider by lazy {
        getGlideInterceptorProvider(mainInjector)
    }

    private val okHttpClientProvider by lazy {
        OkHttpClientProvider(
            okHttpInterceptorsProvider.getInterceptors(),
            glideInterceptorsProvider.getInterceptors()
        )
    }

    private fun createMainInjector() = MainInjector(
        provideApplicationContext = { applicationContext },
        provideNetworkHandler = { NetworkHandler(applicationContext) },
        provideHttpClientProvider = { okHttpClientProvider },
        provideDispatchers = { DispatchersImp },
        provideRepositoriesProvider = { RepositoriesProvider(mainInjector) },
        provideSharedPreferencesHandler = { SharedPreferencesHandler(applicationContext) }
    )
}
