package com.jmlb0003.itcv.data.network.topic

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.NetworkHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.BaseService
import com.jmlb0003.itcv.data.network.topic.response.TopicsResponse
import retrofit2.Response

class TopicsService(
    private val topicsApiClient: TopicsApiClient,
    networkHandler: NetworkHandler,
    gson: Gson
) : BaseService(networkHandler, gson) {

    fun getTopics(username: String, repoName: String): Either<Failure, TopicsResponse> =
        performCall(topicsApiClient.getRepositoryTopics(username, repoName)).let {
            when (val result = it) {
                is Either.Left -> result
                is Either.Right -> handleTopicsResponse(result.rightValue)
            }
        }

    private fun handleTopicsResponse(response: Response<TopicsResponse>) =
        try {
            Either.Right(requireNotNull(response.body()))
        } catch (exception: JsonParseException) {
            Either.Left(Failure.NetworkRequestError(IllegalStateException("There was an error parsing response for topics")))
        }
}
