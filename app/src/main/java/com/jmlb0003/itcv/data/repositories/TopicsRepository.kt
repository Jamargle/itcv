package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.topic.TopicsService
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.domain.repositories.TopicsRepository as TopicsRepositoryInterface

class TopicsRepository(
    private val topicsService: TopicsService,
    private val topicsMappers: TopicsMapper
) : TopicsRepositoryInterface {

    override fun getRepositoryTopics(
        repoName: String,
        repoOwnerUsername: String
    ): Either<Failure, List<Topic>> =
        when (val result = topicsService.getTopics(repoOwnerUsername, repoName)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(topicsMappers.mapToDomain(result.rightValue))
        }
}
