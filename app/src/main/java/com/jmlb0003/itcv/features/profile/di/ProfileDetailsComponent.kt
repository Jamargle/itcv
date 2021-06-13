package com.jmlb0003.itcv.features.profile.di

import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.profile.ProfileDetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

internal const val WEBSITE_BUTTON_CLICKED = "onWebSiteButtonClicked"
internal const val REPO_URL_BUTTON_CLICKED = "onRepoUrlButtonClicked"

@FragmentScope
@Subcomponent(modules = [ProfileDetailsModule::class])
interface ProfileDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance profileDetailsFragment: ProfileDetailsFragment,
            @BindsInstance @Named(WEBSITE_BUTTON_CLICKED) onWebSiteButtonClicked: (String) -> Unit,
            @BindsInstance @Named(REPO_URL_BUTTON_CLICKED) onRepoUrlButtonClicked: (String) -> Unit
        ): ProfileDetailsComponent
    }

    fun inject(profileDetailsFragment: ProfileDetailsFragment)

}
