package com.jmlb0003.itcv.features.home.di

import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.home.HomeFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance homeFragment: HomeFragment
        ): HomeComponent
    }

    fun inject(homeFragment: HomeFragment)

}
