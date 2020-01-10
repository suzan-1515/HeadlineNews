package com.cognota.feed.commons.di

import com.cognota.core.di.CoreComponent
import com.cognota.core.di.ModuleScope
import com.cognota.feed.FeedActivity
import com.cognota.feed.list.di.FeedListComponent
import dagger.Component

@ModuleScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [NetworkModule::class, PersistanceModule::class]
)
interface FeedComponent {

    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating
        // an instance of FeedComponent
        fun create(coreComponent: CoreComponent): FeedComponent
    }

    fun inject(activity: FeedActivity)
    fun feedListComponent(): FeedListComponent.Factory

}
