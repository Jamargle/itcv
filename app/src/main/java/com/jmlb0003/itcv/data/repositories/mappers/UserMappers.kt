package com.jmlb0003.itcv.data.repositories.mappers

import com.jmlb0003.itcv.data.network.user.response.UserResponse
import com.jmlb0003.itcv.domain.model.User

object UserMappers {

    fun mapToDomain(userResponse: UserResponse) =
        with(userResponse) {
            User(
                username = username,
                name = name,
                repositoryCount = reposCount,
                email = email ?: "",
                location = location ?: ""
            )
        }
}
