package com.jmlb0003.itcv.domain.usecases

import com.jmlb0003.itcv.core.interactor.UseCase
import com.jmlb0003.itcv.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateDefaultUserUseCase
@Inject constructor(
    private val usersRepository: UserRepository
) : UseCase<String, UpdateDefaultUserUseCase.Input>() {

    override suspend fun run(params: Input) =
        usersRepository.updateDefaultUser(params.newUsername)

    data class Input(
        val newUsername: String
    )
}
