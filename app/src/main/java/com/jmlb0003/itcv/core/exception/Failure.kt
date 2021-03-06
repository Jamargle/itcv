package com.jmlb0003.itcv.core.exception

sealed class Failure(val error: Throwable? = null) : Throwable(error?.message) {

    object NetworkConnection : Failure()
    class ServerError(error: Throwable) : Failure(error)
    class DatabaseError(error: Throwable) : Failure(error)
    class NetworkRequestError(error: Throwable) : Failure(error)

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure(error: Throwable? = null) : Failure(error)
}
