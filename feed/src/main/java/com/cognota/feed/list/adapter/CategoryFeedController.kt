package com.cognota.feed.list.adapter

import com.airbnb.epoxy.EpoxyController
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.domain.FeedDTO
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject


@FeatureScope
class CategoryFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<FeedDTO> = mutableListOf()
    private var loading: Boolean = false

    fun setFeeds(feeds: List<FeedDTO>?) {
        if (!feeds.isNullOrEmpty()) {
            this.feeds = feeds
            requestModelBuild()
        }
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
        requestModelBuild()
    }

    override fun buildModels() {
        feeds.forEachIndexed { index, feed ->
            if (index % 5 == 0) {
                feedCard(picasso) {
                    id(feed.id)
                    title(feed.title)
                    feed.thumbnail()?.let { image -> image(image) }
                    feed.description?.let { desc -> preview(desc) }
                    feed.source.let { source ->
                        source(source.name)
                        source.favIcon()?.let { icon -> sourceIcon(icon) }
                    }
                    feed.category.let { category -> category(category.name) }
                    date(feed.publishedDate())
                    clickListener { model, parentView, clickedView, position ->
                        Timber.d("Feed list clicked: %s", model.title())
                    }
                }
            } else {
                feedList(picasso) {
                    id(feed.id)
                    title(feed.title)
                    feed.thumbnail()?.let { image -> image(image) }
                    feed.description?.let { desc -> preview(desc) }
                    feed.source.let { source ->
                        source(source.name)
                        source.favIcon()?.let { icon -> sourceIcon(icon) }
                    }
                    feed.category.let { category -> category(category.name) }
                    date(feed.publishedDate())
                    clickListener { model, parentView, clickedView, position ->
                        Timber.d("Feed card clicked: %s", model.title())
                    }
                }
            }


        }

        EmptyModel_().apply {
            id("empty")
            title("No new feed available for this category at the moment! \n Check other categories.")
        }.addIf(feeds.isNullOrEmpty() and !loading, this)

        ProgressModel_().apply {
            id("progress")
        }.addIf(loading, this)

    }


}