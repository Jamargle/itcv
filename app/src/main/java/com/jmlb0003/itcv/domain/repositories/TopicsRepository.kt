package com.jmlb0003.itcv.domain.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.Topic

interface TopicsRepository {

    fun getRepositoryTopics(
        repoId: String,
        repoName: String,
        repoOwnerUsername: String
    ): Either<Failure, List<Topic>>
}
