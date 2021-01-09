package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User

class GetUserProfileUseCase(
    private val usersRepository: UserRepository
) : UseCase<User, GetUserProfileUseCase.Input>() {

    override suspend fun run(params: Input) =
        usersRepository.getUser(params.username)

    data class Input(
        val username: String
    )
}
