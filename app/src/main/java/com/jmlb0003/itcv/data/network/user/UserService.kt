package com.jmlb0003.itcv.data.network.user

import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.BaseService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import retrofit2.Response

class UserService(
    private val userApiClient: UserApiClient,
    networkHandler: NetworkHandler
) : BaseService(networkHandler) {

    fun getUserProfile(username: String): Either<Failure, UserResponse> =
        performCall(userApiClient.getUserProfile(username)).let {
            if (!it.isRight) {
                it as Either.Left
            } else {
                handleUserProfileResponse((it as Either.Right).rightValue)
            }
        }

    private fun handleUserProfileResponse(response: Response<UserResponse>) =
        try {
            with(response) {
                if (isSuccessful) {
                    Either.Right(requireNotNull(body()))
                } else {
                    Either.Left(Failure.ServerError(IllegalStateException("Error while fetching user's profile")))
                }
            }
        } catch (exception: JsonParseException) {
            Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response to fetch user's profile")))
        }
}
