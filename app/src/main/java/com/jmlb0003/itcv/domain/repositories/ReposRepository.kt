package com.jmlb0003.itcv.domain.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.Repo

interface ReposRepository {

    fun getUserRepositories(username: String): Either<Failure, List<Repo>>
}
