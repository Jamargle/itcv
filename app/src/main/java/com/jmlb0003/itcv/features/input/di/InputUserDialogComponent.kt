package com.jmlb0003.itcv.features.input.di

import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.input.InsertUserDialog
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [InputUserDialogModule::class])
interface InputUserDialogComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance insertUserDialog: InsertUserDialog
        ): InputUserDialogComponent
    }

    fun inject(insertUserDialog: InsertUserDialog)

}
