package com.jmlb0003.itcv.core.exception

sealed class Failure(val error: Throwable? = null) : Throwable() {

    object NetworkConnection : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure(error: Throwable? = null) : Failure(error)
}
