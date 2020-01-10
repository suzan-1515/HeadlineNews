package com.cognota.feed.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.domain.*
import com.cognota.feed.list.data.ListDataContract
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class ListViewModel(
    private val feedRepository: ListDataContract.Repository
) : BaseViewModel() {

    private val mutableTrendingFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val trendingFeeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableTrendingFeeds

    private val mutableLatestFeeds: MutableLiveData<StatefulResource<List<FeedWithRelatedFeedDTO>?>> =
        MutableLiveData()
    val latestFeeds: LiveData<StatefulResource<List<FeedWithRelatedFeedDTO>?>> =
        mutableLatestFeeds

    private val mutableSources: MutableLiveData<List<SourceDTO>?> =
        MutableLiveData()
    var sources: LiveData<List<SourceDTO>?> = mutableSources

    private val mutableCategories: MutableLiveData<List<CategoryDTO>?> =
        MutableLiveData()
    var categories: LiveData<List<CategoryDTO>?> = mutableCategories

    private val mutableTags: MutableLiveData<StatefulResource<List<TagDTO>?>> =
        MutableLiveData()
    val tags: LiveData<StatefulResource<List<TagDTO>?>> =
        mutableTags


    fun getLatestFeed() {
        launch {
            Timber.d("Triggered latest feed repo call")
            mutableLatestFeeds.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getLatestFeeds().await()
            when {
                resource.hasData() -> {
                    Timber.d("has data")
                    //return the value
                    mutableLatestFeeds.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableLatestFeeds.value = StatefulResource<List<FeedWithRelatedFeedDTO>?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableLatestFeeds.value = StatefulResource<List<FeedWithRelatedFeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.service_error)
                        }
                else -> mutableLatestFeeds.value = StatefulResource<List<FeedWithRelatedFeedDTO>?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.feed_not_available)
                    }
            }
        }
    }

    fun getTrendingFeed() {
        launch {
            Timber.d("Triggered trending feed repo call")
            mutableTrendingFeeds.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getTop10Feeds().await()
            when {
                resource.hasData() -> {
                    //return the value
                    mutableTrendingFeeds.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.service_error)
                        }
                else -> mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.feed_not_available)
                    }
            }
        }
    }


    fun getSources() {
        launch {
            Timber.d("Triggered sources stream call")
            feedRepository.getFeedSourcesReactive().collect { data ->
                mutableSources.value = data
            }
        }
    }

    fun getCategories() {
        launch {
            Timber.d("Triggered categories stream call")
            feedRepository.getFeedCategoriesReactive().collect { data ->
                mutableCategories.value = data
            }
        }
    }

    fun getTags() {
        launch {
            Timber.d("Triggered tags repo call")
            mutableTags.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getFeedTags().await()
            when {
                resource.hasData() -> {
                    //return the value
                    mutableTags.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableTags.value = StatefulResource<List<TagDTO>?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableTags.value = StatefulResource<List<TagDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.error_fetching_sources)
                        }
                else -> mutableTags.value = StatefulResource<List<TagDTO>?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.sources_not_available)
                    }
            }
        }
    }

}