package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.domain.repositories.UserRepository
import javax.inject.Inject

class GetDefaultUserProfileUseCase
@Inject constructor(
    private val usersRepository: UserRepository
) : UseCase<User, UseCase.None>() {

    override suspend fun run(params: None) =
        usersRepository.getDefaultUser()
}
