package com.cognota.feed.category.adapter

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyController
import com.cognota.feed.R
import com.cognota.feed.category.ui.CategoriesFeedFragmentDirections
import com.cognota.feed.commons.adapter.EmptyModel_
import com.cognota.feed.commons.adapter.ProgressModel_
import com.cognota.feed.commons.adapter.feedCard
import com.cognota.feed.commons.adapter.feedList
import com.cognota.feed.commons.domain.FeedDTO
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CategoryFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<FeedDTO> = mutableListOf()
    private var loading: Boolean = true

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
                    feed(feed)
                    clickListener { model, parentView, clickedView, position ->
                        val extras = FragmentNavigatorExtras(
                            parentView.title to parentView.title.transitionName,
                            parentView.preview to parentView.preview.transitionName,
                            parentView.image to parentView.image.transitionName
                        )
                        if (clickedView.id == R.id.option) {
                            clickedView.findNavController().navigate(
                                CategoriesFeedFragmentDirections.menuAction(
                                    feed = model.feed
                                ),
                                extras
                            )
                        } else {
                            clickedView.findNavController().navigate(
                                CategoriesFeedFragmentDirections.detailAction(
                                    feed = model.feed
                                ),
                                extras
                            )

                        }
                    }
                }
            } else {
                feedList(picasso) {
                    id(feed.id)
                    feed(feed)
                    clickListener { model, parentView, clickedView, position ->
                        val extras = FragmentNavigatorExtras(
                            parentView.title to parentView.title.transitionName,
                            parentView.preview to parentView.preview.transitionName,
                            parentView.image to parentView.image.transitionName
                        )
                        if (clickedView.id == R.id.option) {
                            clickedView.findNavController().navigate(
                                CategoriesFeedFragmentDirections.menuAction(
                                    feed = model.feed
                                ),
                                extras
                            )

                        } else {
                            clickedView.findNavController().navigate(
                                CategoriesFeedFragmentDirections.detailAction(
                                    feed = model.feed
                                ),
                                extras
                            )

                        }
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
        }.addIf(feeds.isNullOrEmpty() and loading, this)

    }


}