package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.SearchResult

class SearchUseCase(
    private val usersRepository: UserRepository
) : UseCase<List<SearchResult>, SearchUseCase.Input>() {

    override suspend fun run(params: Input): Either<Failure, List<SearchResult>> =
        usersRepository.searchUserByUsername(params.usernameQuery)

    data class Input(
        val usernameQuery: String
    )
}
