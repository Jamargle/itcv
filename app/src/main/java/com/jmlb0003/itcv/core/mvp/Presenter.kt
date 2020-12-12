package com.jmlb0003.itcv.core.mvp

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class Presenter(
    private val dispatchers: Dispatchers
) : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.mainThread

    open fun onStart() {}

    open fun clear() = job.cancel()
}
