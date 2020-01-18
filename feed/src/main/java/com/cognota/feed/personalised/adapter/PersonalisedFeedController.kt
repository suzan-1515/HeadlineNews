package com.cognota.feed.personalised.adapter

import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.SnapHelper
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.Carousel.SnapHelperFactory
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.adapter.*
import com.cognota.feed.commons.domain.*
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


@FeatureScope
class PersonalisedFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    init {
        Carousel.setDefaultGlobalSnapHelperFactory(object : SnapHelperFactory() {
            override fun buildSnapHelper(context: Context?): SnapHelper {
                return GravitySnapHelper(Gravity.START)
            }

        })

    }

    private var latestFeeds: List<FeedWithRelatedFeedDTO> = mutableListOf()
    private var trendingFeeds: List<FeedDTO> = mutableListOf()
    private var sources: List<SourceDTO> = mutableListOf()
    private var categories: List<CategoryDTO> = mutableListOf()
    private var tags: List<TagDTO> = mutableListOf()

    fun setTrendingFeeds(trendingFeeds: List<FeedDTO>?) {
        if (!trendingFeeds.isNullOrEmpty()) {
            this.trendingFeeds = trendingFeeds
            requestModelBuild()
        }

    }

    fun setLatestFeeds(feeds: List<FeedWithRelatedFeedDTO>?) {
        if (!feeds.isNullOrEmpty()) {
            this.latestFeeds = feeds
            requestModelBuild()
        }

    }

    fun setSources(sources: List<SourceDTO>?) {
        if (!sources.isNullOrEmpty()) {
            this.sources = sources
            requestModelBuild()
        }

    }

    fun setCategory(categories: List<CategoryDTO>?) {
        if (!categories.isNullOrEmpty()) {
            this.categories = categories
            requestModelBuild()
        }

    }


    fun setTags(tags: List<TagDTO>?) {
        if (!tags.isNullOrEmpty()) {
            this.tags = tags
            requestModelBuild()
        }
    }

    override fun buildModels() {

        if (!trendingFeeds.isNullOrEmpty()) {
            Timber.d("building trending feeds models")
            header {
                id("feed_trending")
                title("Trending news")
            }

            carousel {
                id("trending_carousel")
                paddingDp(0)
                numViewsToShowOnScreen(2.1f)
                models(
                    trendingFeeds.map { feed ->
                        FeedMiniCardModel_(
                            picasso
                        ).apply {
                            id(feed.id)
                            feed(feed)
                        }
                    }

                )
            }
        }

        if (!tags.isNullOrEmpty()) {
            Timber.d("building tags models")
            header {
                id("tags_header")
                title("Trending topics")
            }

            flowCarousel {
                id("tags_carousel")
                paddingDp(0)
                models(
                    tags.map { tag ->
                        TagModel_(picasso).apply {
                            id(tag.title)
                            title(tag.title)
                            tag.icon()?.let { icon -> icon(icon) }
                            clickListener { model, parentView, clickedView, position ->
                                Timber.d("Tag clicked: %s", model.title())
                            }
                        }
                    }
                )
            }

        }

        if (!latestFeeds.isNullOrEmpty()) {
            header {
                id("latest_feed_header")
                title("Top stories for you right now")
            }

            latestFeeds.forEachIndexed { index, feed ->
                if (index == 5) {
                    if (!categories.isNullOrEmpty()) {
                        header {
                            id("categories_header")
                            title("News categories")
                        }

                        flowCarousel {
                            id("categories_carousel")
                            paddingDp(0)
                            models(
                                categories.map { category ->
                                    CategoryModel_(picasso).apply {
                                        id(category.id)
                                        category.icon()?.let { icon -> icon(icon) }
                                        title(category.name)
                                        clickListener { model, parentView, clickedView, position ->
                                            Timber.d("Category clicked: %s", model.title())
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else if (index == 10) {
                    if (!sources.isNullOrEmpty()) {
                        header {
                            id("sources_header")
                            title("News sources")
                        }
                        carousel {
                            id("source_carousel")
                            paddingDp(0)
                            models(
                                sources.map { source ->
                                    SourceModel_(picasso).apply {
                                        id(source.id)
                                        source.icon()?.let { icon -> icon(icon) }
                                        title(source.name)
                                        clickListener { model, parentView, clickedView, position ->
                                            Timber.d("Source clicked: %s", model.title())
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    if (feed.feedWithRelatedFeeds.isNullOrEmpty()) {
                        if (index % 4 == 0) {
                            feedList(picasso) {
                                id(feed.feed.id)
                                feed(feed.feed)
                            }
                        } else {
                            feedCard(picasso) {
                                id(feed.feed.id)
                                feed(feed.feed)
                            }
                        }

                    } else {
                        IndicatorCarousel().apply {
                            id(feed.feed.id)
                            paddingDp(0)
                            models(
                                listOf(
                                    FeedMultiCardModel_(
                                        picasso
                                    ).apply {
                                        id(feed.feed.id)
                                        feed(feed.feed)
                                    }) + feed.feedWithRelatedFeeds.map { related ->
                                    FeedMultiCardModel_(
                                        picasso
                                    ).apply {
                                        id(related.id)
                                        relatedFeed(related)
                                    }
                                }
                            )
                        }.addTo(this)
                    }
                }
            }
        }

        ProgressModel_().apply {
            id("progress")
        }.addIf(
            latestFeeds.isNullOrEmpty()
                    || trendingFeeds.isNullOrEmpty()
                    || tags.isNullOrEmpty()
                    || sources.isNullOrEmpty()
                    || categories.isNullOrEmpty()
            , this
        )

    }

}