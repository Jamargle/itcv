package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.data.local.ReposLocalDataSource
import com.jmlb0003.itcv.data.local.TopicsLocalDataSource
import com.jmlb0003.itcv.data.local.UserLocalDataSource
import com.jmlb0003.itcv.data.local.database.MyDataBase
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
import com.jmlb0003.itcv.utils.GsonUtils

class RepositoriesProvider(
    private val mainInjector: MainInjector
) {

    val userRepository by lazy {
        UserRepository(
            mainInjector.sharedPreferencesHandler,
            userLocalDataSource,
            userService,
            UserMappers,
            SearchResultsMappers
        )
    }
    val repositoriesRepository by lazy {
        ReposRepository(
            reposLocalDataSource,
            repoService,
            ReposMappers
        )
    }
    val topicsRepository by lazy {
        TopicsRepository(
            topicsLocalDataSource,
            topicsService,
            TopicsMapper
        )
    }

    private val userLocalDataSource by lazy {
        UserLocalDataSource(MyDataBase.getInstance(mainInjector.applicationContext))
    }

    private val reposLocalDataSource by lazy {
        ReposLocalDataSource(MyDataBase.getInstance(mainInjector.applicationContext))
    }

    private val topicsLocalDataSource by lazy {
        TopicsLocalDataSource(MyDataBase.getInstance(mainInjector.applicationContext))
    }

    private val networkHandler get() = mainInjector.networkHandler

    private val gsonUtils = GsonUtils

    private val okHttpInterceptorsProvider by lazy {
        getInterceptorProvider(mainInjector)
    }

    private val okHttpClientProvider by lazy {
        OkHttpClientProvider(
            okHttpInterceptorsProvider.getInterceptors()
        )
    }

    private val apiServiceGenerator by lazy {
        ApiServiceGenerator(
            okHttpClientProvider,
            gsonUtils
        )
    }

    // region Services instantiation
    private val userService by lazy {
        UserService(
            userApiClient,
            gsonUtils.gson,
            networkHandler
        )
    }
    private val repoService by lazy {
        RepoService(
            repoApiClient,
            gsonUtils.gson,
            networkHandler
        )
    }
    private val topicsService by lazy {
        TopicsService(
            topicsApiClient,
            networkHandler,
            gsonUtils.gson
        )
    }
    // endregion

    // region Api client instantiation
    private val userApiClient by lazy {
        apiServiceGenerator.createService(
            UserApiClient::class.java
        )
    }
    private val repoApiClient by lazy {
        apiServiceGenerator.createService(
            RepositoryApiClient::class.java
        )
    }
    private val topicsApiClient by lazy {
        apiServiceGenerator.createService(
            TopicsApiClient::class.java
        )
    }
    // endregion
}
