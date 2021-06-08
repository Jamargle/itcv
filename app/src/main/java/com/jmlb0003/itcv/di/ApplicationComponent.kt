package com.jmlb0003.itcv.di

import android.content.Context
import com.jmlb0003.itcv.features.home.di.HomeComponent
import com.jmlb0003.itcv.features.input.di.InputUserDialogComponent
import com.jmlb0003.itcv.features.profile.di.ProfileDetailsComponent
import com.jmlb0003.itcv.features.search.di.SearchComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        OkHttpInterceptorModule::class,
        ApplicationSubComponents::class
    ]
)
interface ApplicationComponent {

    val inputUserDialogComponentFactory: InputUserDialogComponent.Factory

    val homeComponentFactory: HomeComponent.Factory

    val searchComponentFactory: SearchComponent.Factory

    val profileDetailsComponentFactory: ProfileDetailsComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

}
