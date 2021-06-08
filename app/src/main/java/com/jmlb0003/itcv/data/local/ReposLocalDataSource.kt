package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.Repo
import com.jmlb0003.itcv.domain.exception.NoInsertedReposException
import com.jmlb0003.itcv.domain.exception.NoReposException
import com.jmlb0003.itcv.domain.exception.NoReposRemovedException

class ReposLocalDataSource(
    private val database: MyDataBase
) {
    // TODO clean cached repos after some period
    fun saveRepos(repos: List<Repo>): Either<Failure, Unit> =
        try {
            database.reposDao().insertRepos(repos)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(NoInsertedReposException(exception))
        }

    fun getReposByUser(username: String): Either<Failure, List<Repo>> =
        try {
            val repos = database.reposDao().getReposByUser(username)
            Either.Right(repos)
        } catch (exception: Exception) {
            Either.Left(NoReposException(exception))
        }

    fun removeReposForUser(username: String): Either<Failure, Unit> =
        when (val reposToRemove = getReposByUser(username)) {
            is Either.Left -> Either.Left(NoReposRemovedException())
            is Either.Right -> removeRepos(reposToRemove.rightValue)
        }

    private fun removeRepos(repos: List<Repo>): Either<Failure, Unit> =
        try {
            database.reposDao().removeRepos(repos)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(NoReposRemovedException(exception))
        }
}
