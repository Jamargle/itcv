package com.jmlb0003.itcv.features.home.di

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.MainToolbarController
import com.jmlb0003.itcv.features.home.HomeFragment
import com.jmlb0003.itcv.features.home.HomeViewModel
import dagger.Module
import dagger.Provides
import com.jmlb0003.itcv.features.NavigationTriggers as HomeNavigationTriggers

@Module
class HomeModule {

    @FragmentScope
    @Provides
    fun provideHomeViewModel(homeViewModelFactory: HomeViewModel.Factory): HomeViewModel =
        homeViewModelFactory.create(HomeViewModel::class.java)

    @FragmentScope
    @Provides
    fun provideHomeNavigationTriggers(homeFragment: HomeFragment): HomeNavigationTriggers =
        homeFragment.activityViewModels<HomeNavigationTriggers>().value

    @FragmentScope
    @Provides
    fun provideMainToolbarControllerTriggers(homeFragment: HomeFragment): MainToolbarController =
        homeFragment.activityViewModels<MainToolbarController>().value

}
