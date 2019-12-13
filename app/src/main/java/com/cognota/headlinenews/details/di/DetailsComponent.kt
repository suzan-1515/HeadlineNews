package com.cognota.headlinenews.details.di

import com.cognota.headlinenews.details.DetailsActivity
import com.cognota.headlinenews.list.di.ListComponent
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@DetailsScope
@Component(
    dependencies = [ListComponent::class],
    modules = [DetailsModule::class, AndroidSupportInjectionModule::class]
)
interface DetailsComponent {

    fun inject(detailsActivity: DetailsActivity)
}

