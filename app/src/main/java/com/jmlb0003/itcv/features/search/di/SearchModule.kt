package com.jmlb0003.itcv.features.search.di

import androidx.fragment.app.activityViewModels
import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.search.SearchFragment
import com.jmlb0003.itcv.features.search.SearchViewModel
import com.jmlb0003.itcv.features.search.adapter.SearchResultMappers
import dagger.Module
import dagger.Provides
import com.jmlb0003.itcv.features.NavigationTriggers as HomeNavigationTriggers

@Module
class SearchModule {

    @FragmentScope
    @Provides
    fun providePresentationSearchResultMapper(): SearchResultMappers =
        SearchResultMappers

    @FragmentScope
    @Provides
    fun provideSearchViewModel(searchViewModelFactory: SearchViewModel.Factory): SearchViewModel =
        searchViewModelFactory.create(SearchViewModel::class.java)

    @FragmentScope
    @Provides
    fun provideHomeNavigationTriggers(searchFragment: SearchFragment): HomeNavigationTriggers =
        searchFragment.activityViewModels<HomeNavigationTriggers>().value

}
