package com.cognota.feed.bookmark.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.cognota.core.application.CoreApp
import com.cognota.feed.R
import com.cognota.feed.commons.di.SharedComponentProvider
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bookmark_feed_option_dialog.*
import timber.log.Timber
import javax.inject.Inject

class BookmarkFeedOptionDialogFragment : BottomSheetDialogFragment() {

    private val args: BookmarkFeedOptionDialogFragmentArgs by navArgs()

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        Timber.d("attaching bookmark feed option dialog fragment")
        SharedComponentProvider.feedComponent(
            ((requireActivity().applicationContext) as CoreApp)
                .coreComponent
        )
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark_feed_option_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        optionRemoveNews.setOnClickListener {
            Timber.d("Remove bookmark news")
            args.feed?.let {
                feedOptionViewModel.unBookmarkFeed(it)
            }
            dismiss()
        }
    }

    companion object {
        fun newInstance(): BookmarkFeedOptionDialogFragment =
            BookmarkFeedOptionDialogFragment()
    }
}
