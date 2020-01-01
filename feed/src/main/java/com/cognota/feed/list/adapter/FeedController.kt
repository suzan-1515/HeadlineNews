package com.cognota.feed.list.adapter

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.cognota.feed.commons.domain.FeedDTO
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


class FeedController @Inject constructor(private val picasso: Picasso) :
    EpoxyController() {

    private var feeds: List<FeedDTO> = mutableListOf()

    fun setFeeds(feeds: List<FeedDTO>?) {
        feeds?.let {
            this.feeds = it
            requestModelBuild()
        }

    }

    override fun buildModels() {
        header {
            id("header")
            title("Top stories for you right now")
        }

        for (feed in feeds) {
            if (feed.relatedFeed.isNullOrEmpty()) {
                Timber.d("empty related feed for feed: %s", feed.title)
                feedList(picasso) {
                    id(feed.id)
                    title(feed.title)
                    feed.image?.let { image(it) }
                    feed.description?.let { preview(it) }
                    feed.publishedDate?.let { date(it) }
                    clickListener { model, parentView, clickedView, position ->
                        Timber.d("Feed clicked: %s", model.title())
                    }
                }

            } else {
                carousel {
                    id(feed.id)
                    numViewsToShowOnScreen(1.2f)
                    models(
                        feed.relatedFeed.map {
                            FeedCardModel_(picasso).apply {
                                id(it.id)
                                title(it.title)
                                feed.image?.let { image(it) }
                                feed.description?.let { preview(it) }
                                feed.publishedDate?.let { date(it) }
                                clickListener { model, parentView, clickedView, position ->
                                    Timber.d("Feed clicked: %s", model.title())
                                }
                            }

                        }.toMutableList()
                            .plus(
                                FeedCardModel_(picasso).apply {
                                    id(feed.id)
                                    title(feed.title)
                                    feed.image?.let { image(it) }
                                    feed.description?.let { preview(it) }
                                    feed.publishedDate?.let { date(it) }
                                    clickListener { model, parentView, clickedView, position ->
                                        Timber.d("Feed clicked: %s", model.title())
                                    }
                                }
                            )
                    )
                }
            }
        }
    }


}