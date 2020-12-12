package com.jmlb0003.itcv.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val mainThread: CoroutineDispatcher
    val backgroundThread: CoroutineDispatcher
}
