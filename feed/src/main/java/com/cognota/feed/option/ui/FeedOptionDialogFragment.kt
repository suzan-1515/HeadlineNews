package com.cognota.feed.option.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.cognota.core.application.CoreApp
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.option.data.OptionEvent
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_feed_option_dialog.*
import timber.log.Timber
import javax.inject.Inject

class FeedOptionDialogFragment : BottomSheetDialogFragment() {

    private val args: FeedOptionDialogFragmentArgs by navArgs()

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        Timber.d("attaching menu dialog fragment")
        SharedComponentProvider.feedComponent(
            ((requireActivity().applicationContext) as CoreApp)
                .coreComponent
        )
            .inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initialiseObservers()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_option_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.feed?.let { feed ->
            optionHideNews.setOnClickListener {
                feedOptionViewModel.hideFeed(feed)
                dismiss()
            }
            optionMoreNewsLike.setOnClickListener {
                feedOptionViewModel.likeFeed(feed)
                dismiss()
            }
            optionLessNewsLike.setOnClickListener {
                feedOptionViewModel.dislikeFeed(feed)
                dismiss()
            }
            feedOptionViewModel.getBookmarkedStatus(feed.id)
        }

        optionHideNews.text =
            context?.getString(R.string.hide_news)
    }

    private fun initialiseObservers() {
        feedOptionViewModel.bookmarkStatusLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    OptionEvent.BOOKMARKED -> {
                        optionSaveNews.text = getString(R.string.remove_from_bookmark)
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        optionSaveNews.text = getString(R.string.bookmark_this_news)
                    }
                    else -> {
                        Timber.d("Unknown bookmark status event")
                    }
                }
                setBookmarkActionListener(it)
            }
        )
    }

    private fun setBookmarkActionListener(event: OptionEvent) {
        args.feed?.let { feed ->
            when (event) {
                OptionEvent.BOOKMARKED -> {
                    optionSaveNews.setOnClickListener {
                        feedOptionViewModel.unBookmarkFeed(feed)
                        dismiss()
                    }
                }
                OptionEvent.UNBOOKMARKED -> {
                    optionSaveNews.setOnClickListener {
                        feedOptionViewModel.bookmarkFeed(feed)
                        dismiss()
                    }
                }
                else -> {
                }
            }
        }
    }

    companion object {
        fun newInstance(): FeedOptionDialogFragment =
            FeedOptionDialogFragment()
    }
}
