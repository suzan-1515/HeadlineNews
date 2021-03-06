package com.cognota.feed.commons.di

import com.cognota.core.di.CoreComponent
import com.cognota.core.di.ModuleScope
import com.cognota.feed.FeedActivity
import com.cognota.feed.bookmark.di.BookmarkFeedComponent
import com.cognota.feed.bookmark.ui.BookmarkFeedOptionDialogFragment
import com.cognota.feed.category.di.CategoryFeedComponent
import com.cognota.feed.detail.di.DetailFeedComponent
import com.cognota.feed.option.ui.FeedOptionDialogFragment
import com.cognota.feed.personalised.di.PersonalizedFeedComponent
import com.cognota.feed.search.di.SearchFeedComponent
import dagger.Component

@ModuleScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [NetworkModule::class, PersistanceModule::class, RepositoryModule::class, ViewModelModule::class]
)
interface FeedComponent {

    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating
        // an instance of FeedComponent
        fun create(coreComponent: CoreComponent): FeedComponent
    }

    fun inject(activity: FeedActivity)

    fun personalizedFeedComponent(): PersonalizedFeedComponent.Factory
    fun categoryFeedComponent(): CategoryFeedComponent.Factory
    fun savedFeedComponent(): BookmarkFeedComponent.Factory

    fun detailFeedComponent(): DetailFeedComponent.Factory
    fun searchFeedComponent(): SearchFeedComponent.Factory

    fun inject(fragment: FeedOptionDialogFragment)
    fun inject(fragment: BookmarkFeedOptionDialogFragment)

}
