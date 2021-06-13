package com.jmlb0003.itcv.di

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface OkHttpInterceptorModule {

    @Singleton
    @Binds
    fun provideInterceptorsProvider(okHttpInterceptorsProvider: OkHttpInterceptorsProvider): InterceptorsProvider

}
