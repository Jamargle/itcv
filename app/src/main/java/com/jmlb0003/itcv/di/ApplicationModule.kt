package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.core.coroutines.Dispatchers
import com.jmlb0003.itcv.core.coroutines.DispatchersImp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideDispatchers(): Dispatchers = DispatchersImp

}
