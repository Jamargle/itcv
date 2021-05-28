package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.CustomApplication
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.coroutines.Dispatchers

class MainInjector(
    private var provideApplicationContext: () -> Context,
    private var provideNetworkHandler: () -> NetworkHandler,
    private var provideHttpClientProvider: () -> OkHttpClientProvider,
    private var provideDispatchers: () -> Dispatchers,
    private var provideRepositoriesProvider: () -> RepositoriesProvider,
    private var provideSharedPreferencesHandler: () -> SharedPreferencesHandler
) {

    val applicationContext get() = provideApplicationContext()
    val networkHandler get() = provideNetworkHandler()
    val httpClientProvider get() = provideHttpClientProvider()
    val dispatchers get() = provideDispatchers()
    val repositoriesProvider get() = provideRepositoriesProvider()
    val sharedPreferencesHandler get() = provideSharedPreferencesHandler()
}

val Context.mainInjector: MainInjector
    get() {
        val app = checkNotNull(applicationContext as? CustomApplication) { "The context is not an Application context" }
        return app.mainInjector
    }
