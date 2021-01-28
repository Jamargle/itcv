package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.domain.model.User

class UserRepository(
    private val userService: UserService
) {

    fun getUser(username: String): Either<Failure, User> =
        when (val result = userService.getUserProfile(username)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(UserMappers.mapToDomain(result.rightValue))
        }
}
