package com.jmlb0003.itcv

import android.app.Application
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.coroutines.DispatchersImp
import com.jmlb0003.itcv.di.ApplicationComponent
import com.jmlb0003.itcv.di.DaggerApplicationComponent
import com.jmlb0003.itcv.di.MainInjector
import com.jmlb0003.itcv.di.RepositoriesProvider

class CustomApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory().create(applicationContext)
    }

    val mainInjector: MainInjector by lazy { createMainInjector() }

    private fun createMainInjector() = MainInjector(
        provideApplicationContext = { applicationContext },
        provideNetworkHandler = { NetworkHandler(applicationContext) },
        provideDispatchers = { DispatchersImp },
        provideRepositoriesProvider = { RepositoriesProvider(mainInjector) },
        provideSharedPreferencesHandler = { SharedPreferencesHandler(applicationContext) }
    )
}
