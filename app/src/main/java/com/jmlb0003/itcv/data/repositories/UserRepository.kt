package com.jmlb0003.itcv.data.repositories

import com.jmlb0003.itcv.core.Either
import com.jmlb0003.itcv.core.SharedPreferencesHandler
import com.jmlb0003.itcv.core.exception.Failure
import com.jmlb0003.itcv.data.MissingDefaultUserNameFailure
import com.jmlb0003.itcv.data.local.UserLocalDataSource
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.repositories.mappers.SearchResultsMappers
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.domain.model.SearchResult
import com.jmlb0003.itcv.domain.model.User
import com.jmlb0003.itcv.utils.DateExtensions.isOlderThanYesterday
import java.util.Date
import javax.inject.Inject
import com.jmlb0003.itcv.domain.repositories.UserRepository as UserRepositoryInterface

class UserRepository
@Inject constructor(
    private val sharedPreferencesHandler: SharedPreferencesHandler,
    private val userLocalDataSource: UserLocalDataSource,
    private val userService: UserService,
    private val userMappers: UserMappers,
    private val searchResultsMappers: SearchResultsMappers
) : UserRepositoryInterface {

    override fun updateDefaultUser(username: String): Either<Failure, String> {
        sharedPreferencesHandler.defaultUserName = username
        return Either.Right(sharedPreferencesHandler.defaultUserName)
    }

    override fun getDefaultUser(): Either<Failure, User> {
        val username = sharedPreferencesHandler.defaultUserName
        return if (username.isNotEmpty()) {
            getUser(username)
        } else {
            Either.Left(MissingDefaultUserNameFailure)
        }
    }

    override fun getUser(username: String): Either<Failure, User> =
        getStoredUser(username)?.let { cached ->
            Either.Right(userMappers.mapToDomain(cached))
        } ?: fetchUser(username).also { cacheDefaultUser(it) }

    private fun getStoredUser(username: String) =
        when (val result = userLocalDataSource.getUser(username)) {
            is Either.Left -> {
                // Ignore the error and just return null
                null
            }
            is Either.Right -> {
                if (result.rightValue.lastCacheUpdate.isOlderThanYesterday()) {
                    userLocalDataSource.removeUser(username)
                    null
                } else {
                    result.rightValue
                }
            }
        }

    private fun cacheDefaultUser(user: Either<Failure, User>) {
        when (user) {
            is Either.Right -> {
                userLocalDataSource.saveUser(
                    userMappers.mapToData(user.rightValue, Date())
                )
            }
            else -> {
                // NO-OP
            }
        }
    }

    private fun fetchUser(username: String): Either<Failure, User> =
        when (val result = userService.getUserProfile(username)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(userMappers.mapToDomain(result.rightValue))
        }

    override fun searchUserByUsername(query: String): Either<Failure, List<SearchResult>> =
        when (val result = userService.searchUser(query)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(result.rightValue.results.map { searchResultsMappers.mapToDomain(it) })
        }
}
