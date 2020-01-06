package com.cognota.feed.list.adapter


import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.cognota.feed.commons.domain.FeedWithRelatedFeedDTO
import com.squareup.picasso.Picasso
import timber.log.Timber


class FeedCardStackController(
    private val context: Context,
    private val picasso: Picasso
) :
    TypedEpoxyController<FeedWithRelatedFeedDTO>() {

    override fun buildModels(feeds: FeedWithRelatedFeedDTO) {

        feedCardStack(picasso, context) {
            id(feeds.feed.id)
            title(feeds.feed.title)
            feeds.feed.thumbnail()?.let { image -> image(image) }
            feeds.feed.description?.let { desc -> preview(desc) }
            feeds.feed.source.let { source ->
                source(source.name)
                source.icon()?.let { icon ->
                    sourceIcon(icon)
                }
            }
            date(feeds.feed.publishedDate())
            clickListener { model, parentView, clickedView, position ->
                Timber.d("Feed clicked: %s", model.title())
            }
        }

        feeds.feedWithRelatedFeeds.forEach { related ->
            feedCardStack(picasso, context) {
                id(related.id)
                title(related.title)
                related.thumbnail()?.let { image -> image(image) }
                related.description?.let { desc -> preview(desc) }
                related.source.let { source ->
                    source(source.name)
                    source.icon()?.let { icon ->
                        sourceIcon(icon)
                    }
                }
                date(related.publishedDate())
                clickListener { model, parentView, clickedView, position ->
                    Timber.d("Feed clicked: %s", model.title())
                }
            }
        }
    }
}