package com.cognota.feed.search.adapter

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyController
import com.cognota.feed.R
import com.cognota.feed.bookmark.ui.BookmarkFeedFragmentDirections
import com.cognota.feed.commons.adapter.*
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import com.squareup.picasso.Picasso
import timber.log.Timber
import javax.inject.Inject

class SearchFeedController @Inject constructor(
    private val picasso: Picasso
) :
    EpoxyController() {

    private var feeds: List<FeedDTO> = mutableListOf()
    private var tags: List<TagDTO> = mutableListOf()
    private var progress = false
    private var empty = false

    var topicClickAction: ((String) -> Unit)? = null

    fun setFeeds(feeds: List<FeedDTO>?) {
        if (feeds.isNullOrEmpty()) {
            this.feeds = mutableListOf()
            empty = true
        } else {
            this.feeds = feeds
            empty = false
        }
        requestModelBuild()
    }

    fun setTags(tags: List<TagDTO>?) {
        tags?.let {
            this.tags = tags
            requestModelBuild()
        }
    }

    fun setProgress(progress: Boolean) {
        this.progress = progress
        this.empty = false
        requestModelBuild()
    }

    fun setEmpty(empty: Boolean) {
        this.empty = empty
        requestModelBuild()
    }

    override fun buildModels() {

        if (!tags.isNullOrEmpty()) {
            header {
                id("tag_header")
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
                                topicClickAction?.let {
                                    it(model.title())
                                }
                            }
                        }
                    }
                )
            }
        }
        if (!feeds.isNullOrEmpty() && !progress) {
            header {
                id("search_header")
                title("Matching feeds for searched query")
            }

            feeds.forEachIndexed { index, feed ->
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
                                BookmarkFeedFragmentDirections.menuAction(
                                    feed = model.feed
                                ),
                                extras
                            )

                        } else {
                            clickedView.findNavController().navigate(
                                BookmarkFeedFragmentDirections.detailAction(
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
            id("search_empty")
            title("Matching news not found for your query.")
        }.addIf(empty, this)
        ProgressModel_().apply {
            id("progress")
        }.addIf(progress, this)

    }
}