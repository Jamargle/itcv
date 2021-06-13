package com.jmlb0003.itcv.features.search.di

import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.search.SearchFragment
import com.jmlb0003.itcv.features.search.adapter.SearchResult
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance searchFragment: SearchFragment,
            @BindsInstance onResultClicked: (SearchResult) -> Unit,
        ): SearchComponent
    }

    fun inject(searchFragment: SearchFragment)

}
