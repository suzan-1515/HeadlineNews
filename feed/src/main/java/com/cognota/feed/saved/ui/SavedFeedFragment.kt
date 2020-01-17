package com.cognota.feed.saved.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

@FeatureScope
class SavedFeedFragment : BaseFragment() {

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        (activity as FeedActivity).feedComponent
            .savedFeedComponent()
            .create()
            .inject(this)
        super.onAttach(context)
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


    }

    companion object {
        @JvmStatic
        fun newInstance() = SavedFeedFragment()
    }
}
