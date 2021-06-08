package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.ReposLocalDataSource
import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.utils.DateExtensions.isOlderThanYesterday
import java.util.Date
import javax.inject.Inject
import com.jmlb0003.itcv.domain.repositories.ReposRepository as ReposRepositoryInterface

class ReposRepository
@Inject constructor(
    private val reposLocalDataSource: ReposLocalDataSource,
    private val repoService: RepoService,
    private val reposMappers: ReposMappers
) : ReposRepositoryInterface {

    override fun getUserRepositories(username: String): Either<Failure, List<Repo>> =
        getStoredRepos(username).let { cached ->
            if (cached.isNotEmpty()) {
                Either.Right(cached)
            } else {
                fetchRepos(username).also { cacheRepos(username, it) }
            }
        }

    private fun getStoredRepos(username: String) =
        when (val result = reposLocalDataSource.getReposByUser(username)) {
            is Either.Left -> emptyList()
            is Either.Right -> {
                when {
                    result.rightValue.isEmpty() -> emptyList()
                    result.rightValue.first().lastCacheUpdate.isOlderThanYesterday() -> {
                        reposLocalDataSource.removeReposForUser(username)
                        emptyList()
                    }
                    else -> result.rightValue.map { reposMappers.mapToDomain(it) }
                }
            }
        }

    private fun fetchRepos(username: String) =
        when (val result = repoService.getRepositories(username)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(result.rightValue.map { reposMappers.mapToDomain(it) })
        }

    private fun cacheRepos(username: String, repos: Either<Failure, List<Repo>>) {
        when (repos) {
            is Either.Right -> {
                val reposToSave = repos.rightValue.map {
                    reposMappers.mapToData(it, username, Date())
                }
                reposLocalDataSource.saveRepos(reposToSave)
            }
            else -> {
                // NO-OP
            }
        }
    }
}
