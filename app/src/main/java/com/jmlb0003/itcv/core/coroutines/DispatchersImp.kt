package com.jmlb0003.itcv.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

object DispatchersImp : Dispatchers {

    override val mainThread: CoroutineDispatcher by lazy {
        kotlinx.coroutines.Dispatchers.Main
    }

    override val backgroundThread: CoroutineDispatcher by lazy {
        kotlinx.coroutines.Dispatchers.Default
    }
}
