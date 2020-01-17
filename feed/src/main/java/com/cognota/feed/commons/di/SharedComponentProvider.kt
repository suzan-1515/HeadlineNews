package com.cognota.feed.commons.di

import com.cognota.core.di.CoreComponent
import com.cognota.feed.detail.di.DetailFeedComponent
import javax.inject.Singleton

@Singleton
object SharedComponentProvider {

    private var feedComponent: FeedComponent? = null
    private var detailsComponent: DetailFeedComponent? = null

    fun feedComponent(coreComponent: CoreComponent): FeedComponent {
        if (feedComponent == null)
            feedComponent = DaggerFeedComponent.factory().create(coreComponent)
        return feedComponent as FeedComponent
    }

    fun destroyFeedComponent() {
        feedComponent = null
    }

    fun detailsComponent(coreComponent: CoreComponent): DetailFeedComponent {
        if (detailsComponent == null) {
            if (feedComponent == null) {
                feedComponent = feedComponent(coreComponent)
            }
            detailsComponent = feedComponent?.detailFeedComponent()?.create()
        }
        return detailsComponent as DetailFeedComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }

}