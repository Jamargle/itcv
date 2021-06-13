package com.jmlb0003.itcv.features.profile.di

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.profile.ProfileDetailsFragment
import com.jmlb0003.itcv.features.profile.ProfileDetailsViewModel
import com.jmlb0003.itcv.features.profile.adapter.TopicNormalizer
import dagger.Module
import dagger.Provides
import com.jmlb0003.itcv.features.NavigationTriggers as HomeNavigationTriggers

@Module
class ProfileDetailsModule {

    @FragmentScope
    @Provides
    fun provideTopicNormalizer(): TopicNormalizer =
        TopicNormalizer

    @FragmentScope
    @Provides
    fun provideProfileDetailsViewModel(profileDetailsViewModelFactory: ProfileDetailsViewModel.Factory): ProfileDetailsViewModel =
        profileDetailsViewModelFactory.create(ProfileDetailsViewModel::class.java)

    @FragmentScope
    @Provides
    fun provideHomeNavigationTriggers(profileDetailsFragment: ProfileDetailsFragment): HomeNavigationTriggers =
        profileDetailsFragment.activityViewModels<HomeNavigationTriggers>().value

}
