package com.cognota.feed.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.cognota.core.di.FeatureScope
import com.cognota.core.ui.BaseFragment
import com.cognota.feed.FeedActivity
import com.cognota.feed.R
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_feed_detail.*
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@FeatureScope
class DetailFeedFragment : BaseFragment() {

    val args: DetailFeedFragmentArgs by navArgs()

    @Inject
    lateinit var picasso: Picasso

    private var snackBar: Snackbar? = null

    override fun onAttach(context: Context) {
        Timber.d("attaching detail feed fragment")
        (activity as FeedActivity).feedComponent
            ?.detailFeedComponent()
            ?.create()
            ?.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initiateDataListener()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_feed_detail, container, false)
    }

    private fun prepareSharedTransition() {
        args.id?.let { id ->
            args.title?.let {
                ViewCompat.setTransitionName(title, "title$id")
            }
            args.description?.let {
                ViewCompat.setTransitionName(description, "preview$id")
            }
            args.image?.let {
                ViewCompat.setTransitionName(image, "image$id")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("detail fragment: onViewCreated")

//        prepareSharedTransition()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp) // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
        toolbar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }

        toolbar.inflateMenu(R.menu.menu_feed_detail)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_bookmark -> {
                    Toast.makeText(context, "Bookmark menu clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_share -> {
                    Toast.makeText(context, "Share menu clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        args.image?.let { picasso.load(it).into(image) }
        args.title?.let { title.text = it }
        args.description?.let { description.text = it }

        if (savedInstanceState == null) {
        }
    }

    private fun initiateDataListener() {
        //Observe the outcome and update state of the screen  accordingly
//        viewModel.latestFeeds.observe(
//            viewLifecycleOwner,
//            Observer<StatefulResource<List<FeedWithRelatedFeedDTO>?>> { resource ->
//                snackBar?.dismiss()
//
//                when (resource.state) {
//                    StatefulResource.State.SUCCESS -> {
//                        if (resource.hasData()) {
//                            Timber.d("Feed data received ")
//                            personalisedFeedController.setLatestFeeds(resource.getData())
//                        } else {
//                            Timber.d("Empty data received")
//                            snackBar = Snackbar.make(
//                                srlFeeds.rootView,
//                                getString(
//                                    resource.message
//                                        ?: R.string.unknown_error
//                                ),
//                                Snackbar.LENGTH_LONG
//                            )
//                                .setAction(R.string.ok) { snackBar?.dismiss() }
//                        }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_NETWORK -> {
//                        Timber.d("Network error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message
//                                    ?: R.string.no_network_connection
//                            ),
//                            Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_API -> {
//                        Timber.d("Api error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message ?: R.string.service_error
//                            ), Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    else -> Timber.d("Unknown state")
//                }
//            })
//
//        viewModel.trendingFeeds.observe(
//            viewLifecycleOwner,
//            Observer<StatefulResource<List<FeedDTO>?>> { resource ->
//                snackBar?.dismiss()
//
//                when (resource.state) {
//                    StatefulResource.State.SUCCESS -> {
//                        if (resource.hasData()) {
//                            Timber.d("Feed data received ")
//                            personalisedFeedController.setTrendingFeeds(resource.getData())
//                        } else {
//                            Timber.d("Empty data received")
//                            snackBar = Snackbar.make(
//                                srlFeeds.rootView,
//                                getString(
//                                    resource.message
//                                        ?: R.string.unknown_error
//                                ),
//                                Snackbar.LENGTH_LONG
//                            )
//                                .setAction(R.string.ok) { snackBar?.dismiss() }
//                        }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_NETWORK -> {
//                        Timber.d("Network error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message
//                                    ?: R.string.no_network_connection
//                            ),
//                            Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_API -> {
//                        Timber.d("Api error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message ?: R.string.service_error
//                            ), Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    else -> Timber.d("Unknown state")
//                }
//            })
//
//        viewModel.tags.observe(
//            requireActivity(),
//            Observer<StatefulResource<List<TagDTO>?>> { resource ->
//                snackBar?.dismiss()
//                when (resource.state) {
//                    StatefulResource.State.SUCCESS -> {
//                        if (resource.hasData()) {
//                            Timber.d("Feed data received ")
//                            personalisedFeedController.setTags(resource.getData())
//                        } else {
//                            Timber.d("Empty data received")
//                            snackBar = Snackbar.make(
//                                srlFeeds.rootView,
//                                getString(
//                                    resource.message
//                                        ?: R.string.unknown_error
//                                ),
//                                Snackbar.LENGTH_LONG
//                            )
//                                .setAction(R.string.ok) { snackBar?.dismiss() }
//                        }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_NETWORK -> {
//                        Timber.d("Network error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message
//                                    ?: R.string.no_network_connection
//                            ),
//                            Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    StatefulResource.State.ERROR_API -> {
//                        Timber.d("Api error")
//                        snackBar = Snackbar.make(
//                            srlFeeds.rootView,
//                            getString(
//                                resource.message ?: R.string.service_error
//                            ), Snackbar.LENGTH_LONG
//                        )
//                            .setAction(R.string.retry) {
//                                viewModel.getLatestFeed()
//                                snackBar?.dismiss()
//                            }
//                        snackBar?.show()
//                        srlFeeds.isRefreshing = false
//                    }
//                    else -> Timber.d("Unknown state")
//                }
//            })
//
//        viewModel.sources.observe(
//            viewLifecycleOwner,
//            Observer { data ->
//                personalisedFeedController.setSources(data)
//            })
//
//        viewModel.categories.observe(
//            viewLifecycleOwner,
//            Observer { data ->
//                personalisedFeedController.setCategory(data)
//            })

    }

    companion object {
        fun newInstance(): DetailFeedFragment {
            return DetailFeedFragment()
        }
    }
}
