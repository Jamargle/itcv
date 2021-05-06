package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.data.network.repo.RepoService
import com.jmlb0003.itcv.data.network.repo.RepositoryApiClient
import com.jmlb0003.itcv.data.network.topic.TopicsApiClient
import com.jmlb0003.itcv.data.network.topic.TopicsService
import com.jmlb0003.itcv.data.network.user.UserApiClient
import com.jmlb0003.itcv.data.network.user.UserService
import com.jmlb0003.itcv.data.repositories.ReposRepository
import com.jmlb0003.itcv.data.repositories.TopicsRepository
import com.jmlb0003.itcv.data.repositories.UserRepository
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.data.repositories.mappers.SearchResultsMappers
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.utils.ApiServiceGenerator

class RepositoriesProvider(private val mainInjector: MainInjector) {

    val userRepository by lazy {
        UserRepository(
            mainInjector.sharedPreferencesHandler,
            userService,
            UserMappers,
            SearchResultsMappers
        )
    }
    val repositoriesRepository by lazy { ReposRepository(repoService, ReposMappers) }
    val topicsRepository by lazy { TopicsRepository(topicsService, TopicsMapper) }

    private val networkHandler get() = mainInjector.networkHandler
    private val gson by lazy { ApiServiceGenerator.gson }

    // region Services instantiation
    private val userService by lazy {
        UserService(
            userApiClient,
            gson,
            networkHandler
        )
    }
    private val repoService by lazy {
        RepoService(
            repoApiClient,
            gson,
            networkHandler
        )
    }
    private val topicsService by lazy {
        TopicsService(
            topicsApiClient,
            networkHandler,
            gson
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
    private val topicsApiClient by lazy {
        ApiServiceGenerator.createService(
            TopicsApiClient::class.java
        )
    }
    // endregion
}
