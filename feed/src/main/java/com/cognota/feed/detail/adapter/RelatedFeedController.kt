package com.cognota.feed.detail.adapter

import com.airbnb.epoxy.EpoxyController
import com.cognota.feed.commons.adapter.ProgressModel_
import com.cognota.feed.commons.adapter.feedList
import com.cognota.feed.commons.adapter.header
import com.cognota.feed.commons.domain.RelatedFeedDTO
import com.squareup.picasso.Picasso
import javax.inject.Inject

class RelatedFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<RelatedFeedDTO> = mutableListOf()
    private var loading: Boolean = true

    fun setFeeds(feeds: List<RelatedFeedDTO>?) {
        if (!feeds.isNullOrEmpty()) {
            this.feeds = feeds
        } else {
            loading = false
        }
        requestModelBuild()
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
        requestModelBuild()
    }

    override fun buildModels() {
        if (!feeds.isNullOrEmpty()) {
            header {
                id("related_heading")
                title("Related news")
            }
            feeds.forEachIndexed { index, feed ->
                feedList(picasso) {
                    id(feed.id)
                    relatedFeed(feed)
                }
            }
        } else {
            ProgressModel_().apply {
                id("progress")
            }.addIf(loading, this)
        }

    }


}