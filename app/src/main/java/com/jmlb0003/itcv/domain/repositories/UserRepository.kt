package com.jmlb0003.itcv.domain.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.model.User

interface UserRepository {

    fun updateDefaultUser(username: String): Either<Failure, String>

    fun getDefaultUser(): Either<Failure, User>

    fun getUser(username: String): Either<Failure, User>

    fun searchUserByUsername(query: String): Either<Failure, List<SearchResult>>
}
