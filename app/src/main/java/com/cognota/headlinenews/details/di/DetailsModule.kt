package com.cognota.headlinenews.details.di

import com.cognota.headlinenews.details.viewmodel.DetailsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    @DetailsScope
    fun detailViewModelFactory(): DetailsViewModelFactory = DetailsViewModelFactory()

}