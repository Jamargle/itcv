package com.jmlb0003.itcv.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.jmlb0003.itcv.CustomApplication
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.coroutines.Dispatchers

class MainInjector(
    private var provideApplicationContext: () -> Context,
    private var provideNetworkHandler: () -> NetworkHandler,
    private var provideDispatchers: () -> Dispatchers,
    private var provideRepositoriesProvider: () -> RepositoriesProvider,
    private var provideSharedPreferencesHandler: () -> SharedPreferencesHandler
) {

    val applicationContext by lazy { provideApplicationContext() }
    val networkHandler by lazy { provideNetworkHandler() }
    val dispatchers by lazy { provideDispatchers() }
    val repositoriesProvider by lazy { provideRepositoriesProvider() }
    val sharedPreferencesHandler by lazy { provideSharedPreferencesHandler() }
}

val FragmentActivity.mainInjector: MainInjector
    get() {
        val app = checkNotNull(applicationContext as? CustomApplication) { "The context is not an Application context" }
        return app.mainInjector
    }
