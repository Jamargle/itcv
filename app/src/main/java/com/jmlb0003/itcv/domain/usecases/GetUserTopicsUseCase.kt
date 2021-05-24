package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.domain.model.Repo
import com.jmlb0003.itcv.domain.model.Topic
import com.jmlb0003.itcv.domain.repositories.ReposRepository
import com.jmlb0003.itcv.domain.repositories.TopicsRepository

class GetUserTopicsUseCase(
    private val reposRepository: ReposRepository,
    private val topicsRepository: TopicsRepository
) : UseCase<List<Topic>, GetUserTopicsUseCase.Input>() {

    override suspend fun run(params: Input): Either<Failure, List<Topic>> {
        val reposOwner = params.username
        return when (val userRepositories = reposRepository.getUserRepositories(reposOwner)) {
            is Either.Left -> Either.Left(userRepositories.leftValue)
            is Either.Right -> Either.Right(getAllTopics(reposOwner, userRepositories.rightValue))
        }
    }

    private fun getAllTopics(reposOwner: String, repos: List<Repo>): List<Topic> {
        val allTopics = mutableListOf<Topic>()
        repos.forEach { repo ->
            val repoTopics = when (val topics = topicsRepository.getRepositoryTopics(repo.name, reposOwner)) {
                is Either.Left -> emptyList()
                is Either.Right -> topics.rightValue
            }

            allTopics.addAll(repoTopics)
        }
        return allTopics
    }

    data class Input(
        val username: String
    )
}
