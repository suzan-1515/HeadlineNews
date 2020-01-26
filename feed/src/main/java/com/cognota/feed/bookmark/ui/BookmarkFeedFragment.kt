package com.cognota.feed.bookmark.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.bookmark.adapter.BookmarkFeedController
import com.cognota.feed.bookmark.viewmodel.BookmarkFeedViewModel
import com.cognota.feed.bookmark.viewmodel.BookmarkFeedViewModelFactory
import com.cognota.feed.option.data.OptionEvent
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_bookmark_feed.*
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class BookmarkFeedFragment : BaseFragment() {

    @Inject
    lateinit var feedController: BookmarkFeedController

    @Inject
    lateinit var bookmarkFeedViewModelFactory: BookmarkFeedViewModelFactory

    private val bookmarkFeedViewModel: BookmarkFeedViewModel by lazy {
        ViewModelProviders.of(requireActivity(), bookmarkFeedViewModelFactory)
            .get(BookmarkFeedViewModel::class.java)
    }

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
    }

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        (activity as FeedActivity).feedComponent
            .savedFeedComponent()
            .create()
            .inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Saved feed fragment")
        rv.setControllerAndBuildModels(feedController)
        feedController.interaction = {
            bookmarkFeedViewModel.clearBookmarkFeeds()
        }

        if (savedInstanceState == null) {
            bookmarkFeedViewModel.getBookmarkFeeds()
        }

    }

    private fun initiateDataListener() {
//        Observe the outcome and update state of the screen accordingly
        bookmarkFeedViewModel.bookmarkedFeedsLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                feedController.setFeeds(data)
            })
        bookmarkFeedViewModel.bookmarksClearedEventLiveData.observe(
            viewLifecycleOwner,
            Observer {
                snackBar = Snackbar.make(
                    root,
                    getString(
                        R.string.bookmarked_cleared
                    ),
                    Snackbar.LENGTH_LONG
                )
                snackBar?.show()
            })

        feedOptionViewModel.optionEventLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.BOOKMARKED -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                R.string.feed_bookmarked
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        snackBar = Snackbar.make(
                            root,
                            getString(
                                R.string.feed_bookmarked_removed
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    else -> {
                        Timber.d("Unknown bookmark state")
                    }
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookmarkFeedFragment()
    }
}
