package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.core.NetworkHandler

class NetworkInjector(
    private var provideNetworkHandler: () -> NetworkHandler,
    private var provideHttpClientProvider: () -> OkHttpClientProvider
) {

    val networkHandler get() = provideNetworkHandler()
    val httpClientProvider get() = provideHttpClientProvider()
}

val Context.networkInjector: NetworkInjector
    get() {
        return mainInjector.networkInjector
    }
