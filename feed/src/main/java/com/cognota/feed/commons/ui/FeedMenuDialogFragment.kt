package com.cognota.feed.commons.ui

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
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModel
import com.cognota.feed.commons.viewmodel.BookmarkFeedViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed_menu_dialog.*
import timber.log.Timber
import javax.inject.Inject

class FeedMenuDialogFragment : BottomSheetDialogFragment() {

    private val args: FeedMenuDialogFragmentArgs by navArgs()

    private var snackBar: Snackbar? = null

    @Inject
    lateinit var bookmarkViewModelFactory: BookmarkFeedViewModelFactory

    private val bookmarkViewModel: BookmarkFeedViewModel by lazy {
        ViewModelProviders.of(requireActivity(), bookmarkViewModelFactory)
            .get(BookmarkFeedViewModel::class.java)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_menu_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        optionSaveNews.setOnClickListener {
            Timber.d("Save news")
            args.feed?.let {
                bookmarkViewModel.bookmarkFeed(it)
                snackBar = Snackbar.make(
                    root,
                    getString(
                        R.string.feed_bookmarked
                    ),
                    Snackbar.LENGTH_LONG
                )
                snackBar?.show()
            }
            dismiss()
        }
        optionHideNews.setOnClickListener {
            Timber.d("Hide news")
            args.feed?.let {
                //                        bookmarkViewModel.bookmarkFeed(it)
                snackBar = Snackbar.make(
                    root,
                    getString(
                        R.string.source_hidden, it.source.name
                    ),
                    Snackbar.LENGTH_LONG
                )
                snackBar?.show()
            }
            dismiss()

        }
        optionMoreNewsLike.setOnClickListener {
            Timber.d("More news")
            args.feed?.let {
                //                        bookmarkViewModel.bookmarkFeed(it)
                snackBar = Snackbar.make(
                    root,
                    getString(
                        R.string.more_news_message
                    ),
                    Snackbar.LENGTH_LONG
                )
                snackBar?.show()
            }
            dismiss()
        }
        optionLessNewsLike.setOnClickListener {
            Timber.d("Less news")
            args.feed?.let {
                //                        bookmarkViewModel.bookmarkFeed(it)
                snackBar = Snackbar.make(
                    root,
                    getString(
                        R.string.less_news_message
                    ),
                    Snackbar.LENGTH_LONG
                )
                snackBar?.show()
            }
            dismiss()
        }

        optionHideNews.text =
            context?.getString(R.string.hide_news_from_source, args.feed?.source?.name)
    }

    companion object {
        fun newInstance(): FeedMenuDialogFragment =
            FeedMenuDialogFragment()
    }
}
