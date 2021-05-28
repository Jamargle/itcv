package com.jmlb0003.itcv

import android.app.Application
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.coroutines.DispatchersImp
import com.jmlb0003.itcv.di.MainInjector
import com.jmlb0003.itcv.di.NetworkInjector
import com.jmlb0003.itcv.di.OkHttpClientProvider
import com.jmlb0003.itcv.di.RepositoriesProvider
import com.jmlb0003.itcv.di.getGlideInterceptorProvider
import com.jmlb0003.itcv.di.getInterceptorProvider

class CustomApplication : Application() {

    val mainInjector: MainInjector by lazy {
        createMainInjector(networkInjector)
    }

    private val networkInjector: NetworkInjector by lazy {
        createNetworkInjector()
    }

    private val okHttpClientProvider by lazy {
        OkHttpClientProvider(
            getInterceptorProvider(mainInjector).getInterceptors(),
            getGlideInterceptorProvider(mainInjector).getInterceptors()
        )
    }

    private fun createMainInjector(networkInjector: NetworkInjector) = MainInjector(
        provideApplicationContext = { applicationContext },
        provideNetworkInjector = { networkInjector },
        provideDispatchers = { DispatchersImp },
        provideRepositoriesProvider = {
            RepositoriesProvider(
                networkInjector,
                mainInjector.sharedPreferencesHandler
            )
        },
        provideSharedPreferencesHandler = { SharedPreferencesHandler(applicationContext) }
    )

    private fun createNetworkInjector() = NetworkInjector(
        provideNetworkHandler = { NetworkHandler(applicationContext) },
        provideHttpClientProvider = { okHttpClientProvider }
    )
}
