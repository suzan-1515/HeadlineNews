package com.cognota.feed.list.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.list.ui.PersonalizedFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [FeedListModule::class]
)
interface FeedListComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): FeedListComponent
    }

    fun inject(fragment: PersonalizedFeedFragment)

}
