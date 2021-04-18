package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.domain.model.User

class GetDefaultUserProfileUseCase(
    private val usersRepository: UserRepository
) : UseCase<User, UseCase.None>() {

    override suspend fun run(params: None) =
        usersRepository.getDefaultUser()
}
