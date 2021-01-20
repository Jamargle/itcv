package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.data.network.user.UserApiClient
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.utils.ApiServiceGenerator

class RepositoriesProvider(private val mainInjector: MainInjector) {

    val userRepository by lazy { UserRepository(userService) }

    private val networkHandler get() = mainInjector.networkHandler

    // region Services instantiation
    private val userService by lazy {
        UserService(
            userApiClient,
            networkHandler
        )
    }
    // endregion

    // region Api client instantiation
    private val userApiClient by lazy {
        ApiServiceGenerator.createService(
            UserApiClient::class.java
        )
    }
    // endregion
}
