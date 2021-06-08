package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.TopicsLocalDataSource
import com.jmlb0003.itcv.data.network.topic.TopicsService
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.utils.DateExtensions.isOlderThanYesterday
import java.util.Date
import javax.inject.Inject
import com.jmlb0003.itcv.domain.repositories.TopicsRepository as TopicsRepositoryInterface

class TopicsRepository
@Inject constructor(
    private val topicsLocalDataSource: TopicsLocalDataSource,
    private val topicsService: TopicsService,
    private val topicsMappers: TopicsMapper
) : TopicsRepositoryInterface {

    override fun getRepositoryTopics(
        repoId: String,
        repoName: String,
        repoOwnerUsername: String
    ): Either<Failure, List<Topic>> =
        getStoredRepositoryTopics(repoId).let { cached ->
            if (cached.isNotEmpty()) {
                Either.Right(cached)
            } else {
                fetchRepositoryTopics(repoName, repoOwnerUsername)
                    .also { cacheTopics(repoId, it) }
            }
        }

    private fun getStoredRepositoryTopics(repoId: String) =
        when (val result = topicsLocalDataSource.getTopicsByRepo(repoId)) {
            is Either.Left -> emptyList()
            is Either.Right -> {
                when {
                    result.rightValue.isEmpty() -> emptyList()
                    result.rightValue.first().lastCacheUpdate.isOlderThanYesterday() -> {
                        topicsLocalDataSource.removeTopicsByRepo(repoId)
                        emptyList()
                    }
                    else -> result.rightValue.map { topicsMappers.mapToDomain(it) }
                }
            }
        }

    private fun fetchRepositoryTopics(
        repoName: String,
        repoOwnerUsername: String
    ): Either<Failure, List<Topic>> =
        when (val result = topicsService.getTopics(repoOwnerUsername, repoName)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(topicsMappers.mapToDomain(result.rightValue))
        }

    private fun cacheTopics(relatedRepoId: String, topics: Either<Failure, List<Topic>>) {
        when (topics) {
            is Either.Right -> {
                val topicsToSave = topics.rightValue.map {
                    topicsMappers.mapToData(it, relatedRepoId, Date())
                }
                topicsLocalDataSource.saveTopics(topicsToSave)
            }
            else -> {
                // NO-OP
            }
        }
    }
}
