package com.jmlb0003.itcv.data.local

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.model.User
import com.jmlb0003.itcv.domain.exception.NoInsertedUserException
import com.jmlb0003.itcv.domain.exception.NoUserException
import com.jmlb0003.itcv.domain.exception.NoUserRemovedException

class UserLocalDataSource(
    private val database: MyDataBase
) {
    // TODO clean cached users after some period
    fun saveUser(user: User): Either<Failure, Unit> =
        try {
            database.userDao().insertUser(user)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(NoInsertedUserException(exception))
        }

    fun getUser(username: String): Either<Failure, User> =
        try {
            database.userDao().getUser(username)?.let { user ->
                Either.Right(user)
            } ?: Either.Left(NoUserException())
        } catch (exception: Exception) {
            Either.Left(NoUserException(exception))
        }

    fun removeUser(username: String): Either<Failure, Unit> =
        try {
            with(database.userDao()) {
                getUser(username)?.let { user ->
                    removeUser(user)
                    Either.Right(Unit)
                } ?: Either.Left(NoUserRemovedException())
            }
        } catch (exception: Exception) {
            Either.Left(NoUserRemovedException(exception))
        }
}
