package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.ProfileDetails

class GetProfileDetailsUseCase(
    private val usersRepository: UserRepository,
    private val reposRepository: ReposRepository
) : UseCase<ProfileDetails, GetProfileDetailsUseCase.Input>() {

    override suspend fun run(params: Input): Either<Failure, ProfileDetails> {
        val userResult = getUserDetails(params.username)
        val reposResult = getUserRepos(params.username)

        return when {
            userResult is Either.Left -> userResult
            reposResult is Either.Left -> reposResult
            else -> Either.Right(
                ProfileDetails(
                    (userResult as Either.Right).rightValue,
                    (reposResult as Either.Right).rightValue,
                )
            )
        }
    }

    private fun getUserDetails(username: String) = usersRepository.getUser(username)

    private fun getUserRepos(username: String) = reposRepository.getUserRepositories(username)

    data class Input(
        val username: String
    )
}
