package com.jmlb0003.itcv.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jmlb0003.itcv.data.network.repo.RepositoryApiClient
import com.jmlb0003.itcv.data.network.topic.TopicsApiClient
import com.jmlb0003.itcv.data.network.user.UserApiClient
import com.jmlb0003.itcv.utils.ApiServiceGenerator
import com.jmlb0003.itcv.utils.DateExtensions
import dagger.Module
import dagger.Provides
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .setDateFormat(DateExtensions.BACKEND_DATE_FORMAT)
            .create()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideOkHttpClient(okHttpInterceptorsProvider: InterceptorsProvider): OkHttpClientProvider =
        OkHttpClientProvider(okHttpInterceptorsProvider.getInterceptors())

    @Singleton
    @Provides
    fun provideUserApiClient(apiServiceGenerator: ApiServiceGenerator): UserApiClient =
        apiServiceGenerator.createService(UserApiClient::class.java)

    @Singleton
    @Provides
    fun provideRepositoryApiClient(apiServiceGenerator: ApiServiceGenerator): RepositoryApiClient =
        apiServiceGenerator.createService(RepositoryApiClient::class.java)

    @Singleton
    @Provides
    fun provideTopicsApiClient(apiServiceGenerator: ApiServiceGenerator): TopicsApiClient =
        apiServiceGenerator.createService(TopicsApiClient::class.java)

}
