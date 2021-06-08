package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.domain.model.User
import java.util.Date
import com.jmlb0003.itcv.data.model.User as DataUser

object UserMappers {

    fun mapToDomain(userResponse: UserResponse) =
        with(userResponse) {
            User(
                username = username,
                name = name ?: username,
                bio = bioDescription ?: "",
                memberSince = profileCreatedDate,
                email = email ?: "",
                location = location ?: "",
                repositoryCount = reposCount,
                followerCount = followerCount,
                website = userWebsite ?: "",
                twitterAccount = twitterAccount ?: ""
            )
        }

    fun mapToDomain(dataUser: DataUser) =
        with(dataUser) {
            User(
                username = userId,
                name = name,
                bio = bio,
                memberSince = memberSince,
                email = email,
                location = location,
                repositoryCount = repositoryCount,
                followerCount = followerCount,
                website = website,
                twitterAccount = twitterAccount
            )
        }

    fun mapToData(user: User, lastCacheUpdate: Date) =
        with(user) {
            DataUser(
                userId = username,
                name = name,
                bio = bio,
                memberSince = memberSince,
                email = email,
                location = location,
                repositoryCount = repositoryCount,
                followerCount = followerCount,
                website = website,
                twitterAccount = twitterAccount,
                lastCacheUpdate = lastCacheUpdate.time
            )
        }
}
