package com.cognota.feed.detail.adapter

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyController
import com.cognota.feed.commons.adapter.ProgressModel_
import com.cognota.feed.commons.adapter.header
import com.cognota.feed.commons.domain.RelatedFeedDTO
import com.cognota.feed.detail.ui.DetailFeedFragmentDirections
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
                relatedFeedList(picasso) {
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
                            DetailFeedFragmentDirections.detailAction(
                                feed = model.feed().toFeedDto()
                            ),
                            extras
                        )
                    }
                }
            }
        } else {
            ProgressModel_().apply {
                id("progress")
            }.addIf(loading, this)
        }

    }


}