package com.jmlb0003.itcv.features.input.di

import com.jmlb0003.itcv.di.FragmentScope
import com.jmlb0003.itcv.features.input.InsertUserDialogViewModel
import dagger.Module
import dagger.Provides

@Module
class InputUserDialogModule {

    @FragmentScope
    @Provides
    fun provideInsertUserDialogViewModel(insertUserDialogViewModelFactory: InsertUserDialogViewModel.Factory): InsertUserDialogViewModel =
        insertUserDialogViewModelFactory.create(InsertUserDialogViewModel::class.java)

}
