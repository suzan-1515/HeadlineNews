package com.cognota.feed.detail.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.detail.ui.DetailFeedActivity
import com.cognota.feed.detail.ui.DetailFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = [DetailFeedModule::class]
)
interface DetailFeedComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailFeedComponent
    }

    fun inject(activity: DetailFeedActivity)
    fun inject(fragment: DetailFeedFragment)

}
