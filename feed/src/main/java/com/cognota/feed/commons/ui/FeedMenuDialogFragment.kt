package com.cognota.feed.commons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.cognota.feed.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_feed_menu_dialog.*
import timber.log.Timber

class FeedMenuDialogFragment : BottomSheetDialogFragment() {

    val args: FeedMenuDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_menu_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        optionSaveNews.setOnClickListener {
            Timber.d("Save news")
            dismiss()
        }
        optionHideNews.setOnClickListener {
            Timber.d("Hide news")
            dismiss()
        }
        optionMoreNewsLike.setOnClickListener {
            Timber.d("More news")
            dismiss()
        }
        optionLessNewsLike.setOnClickListener {
            Timber.d("Less news")
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
