package com.jmlb0003.itcv.di

import com.jmlb0003.itcv.features.home.di.HomeComponent
import com.jmlb0003.itcv.features.input.di.InputUserDialogComponent
import com.jmlb0003.itcv.features.profile.di.ProfileDetailsComponent
import com.jmlb0003.itcv.features.search.di.SearchComponent
import dagger.Module

@Module(
    subcomponents = [
        HomeComponent::class,
        SearchComponent::class,
        ProfileDetailsComponent::class,
        InputUserDialogComponent::class
    ]
)
class ApplicationSubComponents
