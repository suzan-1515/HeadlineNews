package com.cognota.feed.list.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.FeedActivity
import com.cognota.feed.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FeedCategoryFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as FeedActivity).feedComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_category, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedCategoryFragment()
    }
}
