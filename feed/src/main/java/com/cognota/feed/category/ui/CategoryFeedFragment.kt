package com.cognota.feed.category.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.extensions.waitForTransition
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.category.adapter.CategoryFeedController
import com.cognota.feed.category.viewmodel.CategoryFeedViewModel
import com.cognota.feed.category.viewmodel.CategoryFeedViewModelFactory
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.option.data.OptionEvent
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_category_feed.*
import timber.log.Timber
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class CategoryFeedFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: CategoryFeedViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: CategoryFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CategoryFeedViewModel::class.java)
    }

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
    }

    @Inject
    lateinit var feedController: CategoryFeedController

    private var snackBar: Snackbar? = null

    private var categoryCodeParam: String? = null

    private var page = 1

    override fun onAttach(context: Context) {
        (activity as FeedActivity).categoryFeedComponent
            ?.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        categoryCodeParam?.let { code ->
            viewModel.categoryCode(code)
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryCodeParam = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("category fragment")

        rv.setControllerAndBuildModels(feedController)
        waitForTransition(rv)
        srl.setOnRefreshListener { viewModel.getFeeds(page) }
    }

    private fun initiateDataListener() {
        viewModel.feeds.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        feedController.setLoading(resource.isLoading())
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            feedController.setFeeds(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srl,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                        }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            srl,
                            getString(
                                resource.message
                                    ?: R.string.no_network_connection
                            ),
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getFeeds(page)
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            srl,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getFeeds(page)
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        feedController.setLoading(resource.isLoading())
                        srl.isRefreshing = resource.isLoading()
                    }
                    else -> Timber.d("Unknown state")
                }
            })

        viewModel.categoryCode.observe(viewLifecycleOwner, Observer<String> {
            viewModel.getFeeds(page)
        })

        feedOptionViewModel.optionEventLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.BOOKMARKED -> {
                        snackBar = Snackbar.make(
                            srl,
                            getString(
                                R.string.feed_bookmarked
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        snackBar = Snackbar.make(
                            srl,
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
        fun newInstance(categoryCode: String?): CategoryFeedFragment {
            return CategoryFeedFragment().apply {
                arguments = bundleOf(ARG_PARAM1 to categoryCode)
            }
        }
    }
}
