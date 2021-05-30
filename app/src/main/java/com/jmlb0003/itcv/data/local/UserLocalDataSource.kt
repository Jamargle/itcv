package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.User

class UserLocalDataSource(
//    private val database: UserDatabase
) {

    fun getUser(username: String): Either<Failure, User> {
        // TODO return user from local database
        return Either.Left(Failure.DatabaseError(IllegalStateException()))
    }
}
