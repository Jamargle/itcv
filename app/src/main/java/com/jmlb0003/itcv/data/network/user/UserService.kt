package com.jmlb0003.itcv.data.network.user

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.BaseService
import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.data.network.user.response.search.SearchUserResponse
import retrofit2.Response
import javax.inject.Inject

class UserService
@Inject constructor(
    private val userApiClient: UserApiClient,
    gson: Gson,
    networkHandler: NetworkHandler
) : BaseService(networkHandler, gson) {

    // region getUserProfile
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
            Either.Right(requireNotNull(response.body()))
        } catch (exception: JsonParseException) {
            Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response to fetch user's profile")))
        }
    // endregion

    // region searchUser by profileName
    fun searchUser(usernameQuery: String): Either<Failure, SearchUserResponse> =
        performCall(userApiClient.searchUserByUsername(usernameQuery)).let {
            when (it) {
                is Either.Left -> it
                is Either.Right -> handleSearchUserByProfileNameResponse(it.rightValue)
            }
        }

    private fun handleSearchUserByProfileNameResponse(response: Response<SearchUserResponse>) =
        try {
            Either.Right(requireNotNull(response.body()))
        } catch (exception: JsonParseException) {
            Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response for search")))
        }
    // endregion
}
