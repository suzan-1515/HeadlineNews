package com.cognota.feed.list.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


class FeedController @Inject constructor(
    private val context: Context,
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<FeedDTO> = mutableListOf()
    private var sources: List<SourceDTO> = mutableListOf()
    private var categories: List<CategoryDTO> = mutableListOf()

    fun setFeeds(feeds: List<FeedDTO>?) {
        feeds?.let {
            this.feeds = it
            requestModelBuild()
        }

    }

    fun setSources(feeds: List<SourceDTO>?) {
        feeds?.let {
            this.sources = it
            requestModelBuild()
        }

    }

    fun setCategory(feeds: List<CategoryDTO>?) {
        feeds?.let {
            this.categories = it
            requestModelBuild()
        }

    }

    override fun buildModels() {

        HeaderModel_().apply {
            id("sources_header")
            title("News sources")
        }.addIf(!sources.isNullOrEmpty(), this)

        carousel {
            id("source_carousel")
            models(
                sources.map {
                    TopicModel_(picasso).apply {
                        id(it.id)
                        it.icon()?.let { icon(it) }
                        title(it.name)
                        clickListener { model, parentView, clickedView, position ->
                            Timber.d("Source clicked: %s", model.title())
                        }
                    }
                }.toMutableList()
            )
        }

        HeaderModel_().apply {
            id("feed_header")
            title("Top stories for you right now")
        }.addIf(!feeds.isNullOrEmpty(), this)

        for (feed in feeds) {
            if (feed.relatedFeed.isNullOrEmpty()) {
                Timber.d("empty related feed for feed: %s", feed.title)
                feedList(picasso, context) {
                    id(feed.id)
                    title(feed.title)
                    feed.thumbnail()?.let { image(it) }
                    feed.description?.let { preview(it) }
                    feed.sourceDTO?.let {
                        source(it.name)
                        it.icon()?.let { icon -> sourceIcon(icon) }
                    }
                    date(feed.publishedDate())
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
                            FeedCardModel_(picasso, context).apply {
                                id(it.id)
                                title(it.title)
                                it.thumbnail()?.let { image(it) }
                                it.description?.let { preview(it) }
                                it.sourceDTO?.let { source ->
                                    source(source.name)
                                    source.icon()?.let { icon ->
                                        sourceIcon(icon)
                                    }
                                }
                                date(feed.publishedDate())
                                clickListener { model, parentView, clickedView, position ->
                                    Timber.d("Feed clicked: %s", model.title())
                                }
                            }

                        }.toMutableList()
                            .plus(
                                FeedCardModel_(picasso, context).apply {
                                    id(feed.id)
                                    title(feed.title)
                                    feed.thumbnail()?.let { image(it) }
                                    feed.description?.let { preview(it) }
                                    feed.sourceDTO?.let { source ->
                                        source(source.name)
                                        source.icon()?.let { icon ->
                                            sourceIcon(icon)
                                        }
                                    }
                                    date(feed.publishedDate())
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