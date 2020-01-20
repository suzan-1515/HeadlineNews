package com.cognota.feed.saved.adapter

import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyController
import com.cognota.feed.R
import com.cognota.feed.commons.adapter.feedList
import com.cognota.feed.commons.adapter.header
import com.cognota.feed.commons.domain.BookmarkDTO
import com.cognota.feed.saved.ui.SavedFeedFragmentDirections
import com.squareup.picasso.Picasso
import javax.inject.Inject

class BookmarkFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<BookmarkDTO> = mutableListOf()
    lateinit var interaction: (View) -> Unit

    fun setFeeds(feeds: List<BookmarkDTO>?) {
        feeds?.let {
            this.feeds = feeds
            requestModelBuild()
        }
    }

    override fun buildModels() {
        if (!feeds.isNullOrEmpty()) {
            clearBookmark {
                id("clear_bookmark")
                clickListener { model, parentView, clickedView, position ->
                    interaction(clickedView)
                }
            }
            header {
                id("bookmark_heading")
                title("Saved news")
            }
            feeds.forEachIndexed { index, feed ->
                feedList(picasso) {
                    id(feed.id)
                    feed(feed.toFeedDto())
                    clickListener { model, parentView, clickedView, position ->
                        val extras = FragmentNavigatorExtras(
                            parentView.title to parentView.title.transitionName,
                            parentView.preview to parentView.preview.transitionName,
                            parentView.date to parentView.date.transitionName,
                            parentView.image to parentView.image.transitionName,
                            parentView.sourceIcon to parentView.sourceIcon.transitionName,
                            parentView.category to parentView.category.transitionName
                        )
                        if (clickedView.id == R.id.option) {
                            clickedView.findNavController().navigate(
                                SavedFeedFragmentDirections.menuAction(
                                    feed = model.feed
                                ),
                                extras
                            )

                        } else {
                            clickedView.findNavController().navigate(
                                SavedFeedFragmentDirections.detailAction(
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

    interface Interaction {
        fun onClearBookmark(view: View)
    }


}