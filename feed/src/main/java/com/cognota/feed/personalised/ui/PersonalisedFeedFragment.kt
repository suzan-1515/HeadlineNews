package com.cognota.feed.personalised.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import com.cognota.feed.option.data.OptionEvent
import com.cognota.feed.option.viewmodel.FeedOptionViewModel
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import com.cognota.feed.personalised.adapter.PersonalisedFeedController
import com.cognota.feed.personalised.viewmodel.PersonalizedFeedViewModel
import com.cognota.feed.personalised.viewmodel.PersonalizedFeedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_personalised_feed.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@FeatureScope
class PersonalisedFeedFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: PersonalizedFeedViewModelFactory

    @Inject
    lateinit var personalisedFeedController: PersonalisedFeedController

    @ExperimentalCoroutinesApi
    private val viewModel: PersonalizedFeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PersonalizedFeedViewModel::class.java)
    }

    @Inject
    lateinit var feedOptionViewModelFactory: FeedOptionViewModelFactory

    private val feedOptionViewModel: FeedOptionViewModel by lazy {
        ViewModelProviders.of(requireActivity(), feedOptionViewModelFactory)
            .get(FeedOptionViewModel::class.java)
    }

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        Timber.d("attaching personalised fragment")
        (activity as FeedActivity).feedComponent
            .personalizedFeedComponent()
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
        return inflater.inflate(R.layout.fragment_personalised_feed, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("personalized fragment: onViewCreated")
        rvFeeds.setControllerAndBuildModels(personalisedFeedController)
        srlFeeds.setOnRefreshListener {
            viewModel.getLatestFeed()
            viewModel.getTrendingFeed()
            viewModel.getTags()
        }

        viewModel.getLatestFeed()
        viewModel.getTrendingFeed()
        viewModel.getTags()

    }

    @ExperimentalCoroutinesApi
    private fun initiateDataListener() {
        //Observe the outcome and update state of the screen  accordingly
        viewModel.latestFeeds.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.LOADING -> {
                        srlFeeds.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            personalisedFeedController.setLatestFeeds(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srlFeeds,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
                                ),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.ok) { snackBar?.dismiss() }
                        }
                        snackBar?.show()
                        srlFeeds.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_NETWORK -> {
                        Timber.d("Network error")
                        snackBar = Snackbar.make(
                            srlFeeds,
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
                        srlFeeds.isRefreshing = resource.isLoading()
                    }
                    StatefulResource.State.ERROR_API -> {
                        Timber.d("Api error")
                        snackBar = Snackbar.make(
                            srlFeeds,
                            getString(
                                resource.message ?: R.string.service_error
                            ), Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.retry) {
                                viewModel.getLatestFeed()
                                snackBar?.dismiss()
                            }
                        snackBar?.show()
                        srlFeeds.isRefreshing = resource.isLoading()
                    }
                    else -> Timber.d("Unknown state")
                }
            })

        viewModel.trendingFeeds.observe(
            viewLifecycleOwner,
            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
                snackBar?.dismiss()

                when (resource.state) {
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            personalisedFeedController.setTrendingFeeds(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srlFeeds,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
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
                            srlFeeds,
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
                            srlFeeds,
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

        viewModel.tags.observe(
            requireActivity(),
            Observer<StatefulResource<List<TagDTO>?>> { resource ->
                snackBar?.dismiss()
                when (resource.state) {
                    StatefulResource.State.SUCCESS -> {
                        if (resource.hasData()) {
                            Timber.d("Feed data received ")
                            personalisedFeedController.setTags(resource.getData())
                        } else {
                            Timber.d("Empty data received")
                            snackBar = Snackbar.make(
                                srlFeeds,
                                getString(
                                    resource.message
                                        ?: R.string.unknown_error
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
                            srlFeeds,
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
                            srlFeeds,
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

        viewModel.sources.observe(
            viewLifecycleOwner,
            Observer { data ->
                personalisedFeedController.setSources(data)
            })

        viewModel.categories.observe(
            viewLifecycleOwner,
            Observer { data ->
                personalisedFeedController.setCategory(data)
            })

        feedOptionViewModel.optionEventLiveData.observe(
            viewLifecycleOwner,
            Observer { data ->
                when (data) {
                    OptionEvent.BOOKMARKED -> {
                        snackBar = Snackbar.make(
                            srlFeeds,
                            getString(
                                R.string.feed_bookmarked
                            ),
                            Snackbar.LENGTH_LONG
                        )
                        snackBar?.show()
                    }
                    OptionEvent.UNBOOKMARKED -> {
                        snackBar = Snackbar.make(
                            srlFeeds,
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
        fun newInstance(): PersonalisedFeedFragment {
            return PersonalisedFeedFragment()
        }
    }
}
