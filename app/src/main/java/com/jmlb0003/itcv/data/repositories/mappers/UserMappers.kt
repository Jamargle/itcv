package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.domain.model.User

object UserMappers {

    fun mapToDomain(userResponse: UserResponse) =
        with(userResponse) {
            User(
                avatarUrl = avatar,
                username = username,
                name = name ?: username,
                bio = bioDescription ?: "",
                memberSince = profileCreatedDate,
                email = email ?: "",
                location = location ?: "",
                repositoryCount = reposCount,
                followerCount = followerCount,
                website = userWebsite ?: "",
                twitterAccount = twitterAccount ?: "",
            )
        }
}
