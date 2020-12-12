package com.jmlb0003.itcv.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatchersImp : com.jmlb0003.itcv.core.coroutines.Dispatchers {
    override val mainThread: CoroutineDispatcher by lazy { Dispatchers.Main }
    override val backgroundThread: CoroutineDispatcher by lazy { Dispatchers.Default }
}
