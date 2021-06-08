package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.data.local.database.MyDataBase
import com.jmlb0003.itcv.data.repositories.mappers.ReposMappers
import com.jmlb0003.itcv.data.repositories.mappers.SearchResultsMappers
import com.jmlb0003.itcv.data.repositories.mappers.TopicsMapper
import com.jmlb0003.itcv.data.repositories.mappers.UserMappers
import com.jmlb0003.itcv.domain.repositories.ReposRepository
import com.jmlb0003.itcv.domain.repositories.TopicsRepository
import com.jmlb0003.itcv.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.jmlb0003.itcv.data.repositories.ReposRepository as ReposRepositoryImpl
import com.jmlb0003.itcv.data.repositories.TopicsRepository as TopicsRepositoryImpl
import com.jmlb0003.itcv.data.repositories.UserRepository as UserRepositoryImpl

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMyDataBase(context: Context): MyDataBase =
        MyDataBase.getInstance(context)

    @Singleton
    @Provides
    fun provideUserMappers(): UserMappers = UserMappers

    @Singleton
    @Provides
    fun provideSearchResultsMappers(): SearchResultsMappers = SearchResultsMappers

    @Singleton
    @Provides
    fun provideReposMappers(): ReposMappers = ReposMappers

    @Singleton
    @Provides
    fun provideTopicsMapper(): TopicsMapper = TopicsMapper

    @Singleton
    @Provides
    fun provideUsersRepositoryImpl(usersRepositoryImpl: UserRepositoryImpl): UserRepository =
        usersRepositoryImpl

    @Singleton
    @Provides
    fun provideReposRepositoryImpl(reposRepositoryImpl: ReposRepositoryImpl): ReposRepository =
        reposRepositoryImpl

    @Singleton
    @Provides
    fun provideTopicsRepositoryImpl(topicsRepositoryImpl: TopicsRepositoryImpl): TopicsRepository =
        topicsRepositoryImpl

}
