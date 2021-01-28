package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.network.repo.RepositoryApiClient
import com.jmlb0003.itcv.data.network.user.UserApiClient
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.utils.ApiServiceGenerator

class RepositoriesProvider(private val mainInjector: MainInjector) {

    val userRepository by lazy { UserRepository(userService) }
    val repositoriesRepository by lazy { ReposRepository(repoService) }

    private val networkHandler get() = mainInjector.networkHandler

    // region Services instantiation
    private val userService by lazy {
        UserService(
            userApiClient,
            networkHandler
        )
    }
    private val repoService by lazy {
        RepoService(
            repoApiClient,
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
    private val repoApiClient by lazy {
        ApiServiceGenerator.createService(
            RepositoryApiClient::class.java
        )
    }
    // endregion
}
