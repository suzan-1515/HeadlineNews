package com.cognota.feed.search.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.search.FeedSearchActivity
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [DetailFeedModule::class]
)
interface SearchFeedComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchFeedComponent
    }

    fun inject(activity: FeedSearchActivity)

}
