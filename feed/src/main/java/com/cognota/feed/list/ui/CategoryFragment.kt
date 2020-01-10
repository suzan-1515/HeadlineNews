package com.cognota.feed.list.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import timber.log.Timber

class CategoryFragment : BaseFragment() {

    override fun onAttach(context: Context) {
        (activity as FeedActivity).feedComponent
            .feedListComponent()
            .create()
            .inject(this)
        initiateDataListener()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("category fragment")

    }

    private fun initiateDataListener() {}

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
