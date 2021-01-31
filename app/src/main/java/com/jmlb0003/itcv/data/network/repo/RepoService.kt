package com.jmlb0003.itcv.data.network.repo

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.BaseService
import com.jmlb0003.itcv.data.network.repo.response.RepoResponse
import retrofit2.Response

class RepoService(
    private val repositoryApiClient: RepositoryApiClient,
    gson: Gson,
    networkHandler: NetworkHandler
) : BaseService(networkHandler, gson) {

    // region getRepositories
    fun getRepositories(username: String): Either<Failure, List<RepoResponse>> =
        performCall(repositoryApiClient.getRepositoriesForUser(username)).let {
            when (val result = it) {
                is Either.Left -> result
                is Either.Right -> handleRepositoriesResponse(result.rightValue)
            }
        }

    private fun handleRepositoriesResponse(response: Response<List<RepoResponse>>) =
        try {
            Either.Right(requireNotNull(response.body()))
        } catch (exception: JsonParseException) {
            Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response to user's repositories")))
        }
    // endregion
}
