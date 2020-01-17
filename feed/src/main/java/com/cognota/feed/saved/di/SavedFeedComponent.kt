package com.cognota.feed.saved.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.saved.ui.SavedFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = []
)
interface SavedFeedComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SavedFeedComponent
    }

    fun inject(fragment: SavedFeedFragment)

}
