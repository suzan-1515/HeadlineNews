package com.cognota.feed.list.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.list.viewmodel.ListViewModel
import com.cognota.feed.list.viewmodel.ListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_personalized_feed.*
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PersonalizedFeedFragment : BaseFragment(), PersonalizedFeedAdapter.Interaction {

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var adapterPersonalized: PersonalizedFeedAdapter

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as FeedActivity).feedComponent
            .feedListComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personalized_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("personalized fragment")

        adapterPersonalized.interaction = this
        rvFeeds.adapter = adapterPersonalized
        srlFeeds.setOnRefreshListener { viewModel.getLatestFeed() }

        initiateDataListener()
        viewModel.getLatestFeed()
    }

    private fun initiateDataListener() {
        //Observe the outcome and update state of the screen  accordingly
        viewModel.latestFeeds.observe(
            requireActivity(),
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        srlFeeds.isRefreshing = true
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Data received %d", resource.getData()?.size)
                            adapterPersonalized.swapData(resource.getData()!!)
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srlFeeds.rootView,
                                getString(
                                    resource.message
                                        ?: R.string.feed_not_available
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                        }
                        snackBar?.show()
                        srlFeeds.isRefreshing = false
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            srlFeeds.rootView,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getLatestFeed()
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        srlFeeds.isRefreshing = false
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            srlFeeds.rootView,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getLatestFeed()
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        srlFeeds.isRefreshing = false
                    }
                    else -> Timber.d("Unknown state")
                }
            })
    }

    override fun feedClicked(
        feed: FeedDTO,
        tvTitle: TextView,
        tvBody: TextView,
        tvAuthorName: TextView,
        ivAvatar: ImageView
    ) {

    }

    companion object {
        fun newInstance(): PersonalizedFeedFragment {
            return PersonalizedFeedFragment()
        }
    }
}
