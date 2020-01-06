package com.cognota.feed.list.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.PersonalisedFeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


class FeedController @Inject constructor(
    private val context: Context,
    private val picasso: Picasso
) :
    EpoxyController() {

    private lateinit var personalisedFeeds: PersonalisedFeedDTO
    private var feeds: List<FeedDTO> = mutableListOf()
    private var sources: List<SourceDTO> = mutableListOf()
    private var categories: List<CategoryDTO> = mutableListOf()

    fun setData(personalisedFeeds: PersonalisedFeedDTO?) {
        personalisedFeeds?.let {
            this.personalisedFeeds = personalisedFeeds
            requestModelBuild()
        }
    }

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

        if (::personalisedFeeds.isInitialized) {
            personalisedFeeds.sources?.let {
                header {
                    id("sources_header")
                    title("News sources")
                }

                carousel {
                    id("source_carousel")
                    paddingDp(0)
                    models(
                        it.map { source ->
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

            personalisedFeeds.categories?.let {
                header {
                    id("categories_header")
                    title("News categories")
                }

                carousel {
                    id("categories_carousel")
                    paddingDp(4)
                    models(
                        it.map { category ->
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

            personalisedFeeds.feeds?.let {
                header {
                    id("feed_header")
                    title("Top stories for you right now")
                }

                for (feed in it) {
                    if (feed.feedWithRelatedFeeds.isNullOrEmpty()) {
                        feedList(picasso, context) {
                            id(feed.feed.id)
                            title(feed.feed.title)
                            feed.feed.thumbnail()?.let { image -> image(image) }
                            feed.feed.description?.let { desc -> preview(desc) }
                            feed.feed.source.let { source ->
                                source(source.name)
                                source.icon()?.let { icon -> sourceIcon(icon) }
                            }
                            date(feed.feed.publishedDate())
                            clickListener { model, parentView, clickedView, position ->
                                Timber.d("Feed clicked: %s", model.title())
                            }
                        }

                    } else {
                        carousel {
                            id(feed.feed.id)
                            paddingDp(0)
                            numViewsToShowOnScreen(1.05f)
                            models(
                                listOf(
                                    FeedMultiCardModel_(
                                        picasso,
                                        context
                                    ).apply {
                                        id(feed.feed.id)
                                        title(feed.feed.title)
                                        feed.feed.thumbnail()?.let { image -> image(image) }
                                        feed.feed.description?.let { desc -> preview(desc) }
                                        feed.feed.source.let { source ->
                                            source(source.name)
                                            source.icon()?.let { icon ->
                                                sourceIcon(icon)
                                            }
                                        }
                                        date(feed.feed.publishedDate())
                                        clickListener { model, parentView, clickedView, position ->
                                            Timber.d("Feed clicked: %s", model.title())
                                        }
                                    }) + feed.feedWithRelatedFeeds.map { related ->
                                    FeedMultiCardModel_(
                                        picasso,
                                        context
                                    ).apply {
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
                            )
                        }
                    }
                }
            }
        } else {
            progress {
                id("progress")
            }
        }
    }


}