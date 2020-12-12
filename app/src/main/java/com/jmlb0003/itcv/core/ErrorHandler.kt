package com.jmlb0003.itcv.core

import android.util.Log
import com.jmlb0003.itcv.core.exception.Failure

object ErrorHandler {

    fun handleError(failure: Failure) {
        logError(failure)
    }

    fun handleError(error: Throwable) {
        logError(error)
    }

    private fun logError(error: Throwable) {
        Log.e(error.javaClass.name, error.message ?: error.stackTraceToString())
    }
}
