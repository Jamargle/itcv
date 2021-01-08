package com.jmlb0003.itcv.data.network

import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

abstract class BaseService(
    private val networkHandler: NetworkHandler
) {

    protected fun <T> performCall(call: Call<T>): Either<Failure, Response<T>> =
        if (!networkHandler.isConnected) {
            Either.Left(Failure.NetworkConnection)
        } else {
            try {
                Either.Right(call.execute())
            } catch (exception: JsonParseException) {
                Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response")))
            } catch (exception: IOException) {
                Either.Left(Failure.ServerError(IllegalStateException("Error talking to the server for the request ${call.request().url}")))
            } catch (exception: RuntimeException) {
                Either.Left(Failure.NetworkRequestError(IllegalStateException("Error while performing the request ${call.request().url}")))
            }
        }
}
