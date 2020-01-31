package com.cognota.feed.search.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.search.ui.SearchFeedActivity
import com.cognota.feed.search.ui.SearchFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [SearchFeedModule::class]
)
interface SearchFeedComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchFeedComponent
    }

    fun inject(feedActivity: SearchFeedActivity)
    fun inject(fragment: SearchFeedFragment)

}
