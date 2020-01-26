package com.cognota.feed.bookmark.di

import com.cognota.core.di.FeatureScope
import com.cognota.feed.bookmark.ui.BookmarkFeedFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent(
    modules = []
)
interface BookmarkFeedComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BookmarkFeedComponent
    }

    fun inject(fragment: BookmarkFeedFragment)

}
