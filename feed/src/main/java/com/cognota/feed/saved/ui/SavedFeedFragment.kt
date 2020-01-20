package com.cognota.feed.saved.ui

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
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModel
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModelFactory
import com.cognota.feed.saved.adapter.BookmarkFeedController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_feed.*
import timber.log.Timber
import javax.inject.Inject

@FeatureScope
class SavedFeedFragment : BaseFragment() {

    @Inject
    lateinit var feedController: BookmarkFeedController

    @Inject
    lateinit var viewModelFactory: BookmarkFeedViewModelFactory

    private val viewModel: BookmarkFeedViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(BookmarkFeedViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_saved_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Saved feed fragment")
        rv.setControllerAndBuildModels(feedController)
        feedController.interaction = {
            viewModel.clearBookmarkFeeds()
        }

    }

    private fun initiateDataListener() {
//        Observe the outcome and update state of the screen accordingly
        viewModel.bookmarkFeeds.observe(
            viewLifecycleOwner,
            Observer { data ->
                feedController.setFeeds(data)
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedFeedFragment()
    }
}
