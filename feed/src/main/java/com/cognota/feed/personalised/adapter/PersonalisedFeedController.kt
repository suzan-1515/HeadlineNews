package com.cognota.feed.personalised.adapter

import android.content.Context
import android.view.Gravity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.SnapHelper
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.Carousel.SnapHelperFactory
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.adapter.*
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import com.cognota.feed.commons.domain.TagDTO
import com.cognota.feed.personalised.ui.PersonalisedFeedFragmentDirections
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card_feed.view.*
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

    private var latestFeeds: List<FeedDTO> = mutableListOf()
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

    fun setLatestFeeds(feeds: List<FeedDTO>?) {
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
                            clickListener { model, parentView, clickedView, position ->
                                val extras = FragmentNavigatorExtras(
                                    clickedView.title to clickedView.title.transitionName,
                                    clickedView.date to clickedView.date.transitionName,
                                    clickedView.image to clickedView.image.transitionName,
                                    clickedView.sourceIcon to clickedView.sourceIcon.transitionName
                                )
                                clickedView.findNavController().navigate(
                                    PersonalisedFeedFragmentDirections.detailAction(
                                        feed = model.feed
                                    ),
                                    extras
                                )
                            }
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
                    if ((index % 6) in listOf(1, 2, 3)) {
                        feedList(picasso) {
                            id(feed.id)
                            feed(feed)
                            clickListener { model, parentView, clickedView, position ->
                                Timber.d("Feed list clicked")
                                val extras = FragmentNavigatorExtras(
                                    parentView.title to parentView.title.transitionName,
                                    parentView.preview to parentView.preview.transitionName,
                                    parentView.date to parentView.date.transitionName,
                                    parentView.image to parentView.image.transitionName,
                                    parentView.sourceIcon to parentView.sourceIcon.transitionName,
                                    parentView.category to parentView.category.transitionName
                                )
                                clickedView.findNavController().navigate(
                                    PersonalisedFeedFragmentDirections.detailAction(
                                        feed = model.feed
                                    ),
                                    extras
                                )
                            }
                        }
                    } else {
                        feedCard(picasso) {
                            id(feed.id)
                            feed(feed)
                            clickListener { model, parentView, clickedView, position ->
                                val extras = FragmentNavigatorExtras(
                                    parentView.title to parentView.title.transitionName,
                                    parentView.preview to parentView.preview.transitionName,
                                    parentView.date to parentView.date.transitionName,
                                    parentView.image to parentView.image.transitionName,
                                    parentView.sourceIcon to parentView.sourceIcon.transitionName,
                                    parentView.category to parentView.category.transitionName
                                )
                                clickedView.findNavController().navigate(
                                    PersonalisedFeedFragmentDirections.detailAction(
                                        feed = model.feed
                                    ),
                                    extras
                                )
                            }
                        }
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