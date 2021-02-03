package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.domain.model.Repo

class ReposRepository(
    private val repoService: RepoService,
    private val reposMappers: ReposMappers
) {

    fun getUserRepositories(username: String): Either<Failure, List<Repo>> =
        when (val result = repoService.getRepositories(username)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(result.rightValue.map { reposMappers.mapToDomain(it) })
        }
}
