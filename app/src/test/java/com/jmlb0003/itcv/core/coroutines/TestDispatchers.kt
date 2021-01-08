package com.jmlb0003.itcv.core.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDispatchers(private val testDispatcher: TestCoroutineDispatcher) : Dispatchers {
    override val mainThread by lazy { testDispatcher }
    override val backgroundThread by lazy { testDispatcher }
}
