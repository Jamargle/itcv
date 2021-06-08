package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.Topic
import com.jmlb0003.itcv.domain.exception.NoInsertedTopicsException
import com.jmlb0003.itcv.domain.exception.NoTopicsException
import com.jmlb0003.itcv.domain.exception.NoTopicsRemovedException
import javax.inject.Inject

class TopicsLocalDataSource
@Inject constructor(
    private val database: MyDataBase
) {
    // TODO clean cached topics after some period
    fun saveTopics(topics: List<Topic>): Either<Failure, Unit> =
        try {
            database.topicsDao().insertTopics(topics)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(NoInsertedTopicsException(exception))
        }

    fun getTopicsByRepo(repoId: String): Either<Failure, List<Topic>> =
        try {
            val topics = database.topicsDao().getTopicsByRepo(repoId)
            Either.Right(topics)
        } catch (exception: Exception) {
            Either.Left(NoTopicsException(exception))
        }

    fun removeTopicsByRepo(repoId: String): Either<Failure, Unit> =
        when (val topicsToRemove = getTopicsByRepo(repoId)) {
            is Either.Left -> Either.Left(NoTopicsRemovedException())
            is Either.Right -> removeTopics(topicsToRemove.rightValue)
        }

    private fun removeTopics(topics: List<Topic>): Either<Failure, Unit> =
        try {
            database.topicsDao().removeTopics(topics)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(NoTopicsRemovedException(exception))
        }
}
