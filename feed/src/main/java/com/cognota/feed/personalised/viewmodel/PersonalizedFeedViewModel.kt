package com.cognota.feed.personalised.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.data.model.api.ApiErrorException
import com.cognota.core.data.model.api.NetworkErrorException
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.core.vo.Status
import com.cognota.feed.R
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import com.cognota.feed.commons.domain.TagDTO
import com.cognota.feed.personalised.data.PersonalizedFeedDataContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class PersonalizedFeedViewModel(
    private val feedRepository: PersonalizedFeedDataContract.Repository,
    private val sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository
) : BaseViewModel() {

    private val mutableTrendingFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val trendingFeeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableTrendingFeeds

    private val mutableLatestFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val latestFeeds: LiveData<StatefulResource<List<FeedDTO>?>> =
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

    init {
        getSources()
        getCategories()
    }

    @ExperimentalCoroutinesApi
    fun getLatestFeed() {
        viewModelScope.launch {
            Timber.d("Triggered latest feed repo call")
            feedRepository.getLatestFeeds().collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Timber.d("has resource")
                        //return the value
                        if (resource.data.isNullOrEmpty())
                            mutableLatestFeeds.value = StatefulResource.loading()
                        else
                            mutableLatestFeeds.value = StatefulResource.success(resource)
                    }
                    Status.SUCCESS -> {
                        mutableLatestFeeds.value = StatefulResource.success(resource)
                    }
                    Status.ERROR -> {
                        when (resource.error) {
                            is ApiErrorException -> {
                                mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR_API)
                                        setMessage(R.string.service_error)
                                    }
                            }
                            is NetworkErrorException -> {
                                mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setMessage(R.string.no_network_connection)
                                        setState(StatefulResource.State.ERROR_NETWORK)
                                    }
                            }
                            else -> {
                                mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR)
                                        setMessage(R.string.unknown_error)
                                    }
                            }
                        }
                    }
                    else -> mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getTrendingFeed() {
        viewModelScope.launch {
            Timber.d("Triggered trending feed repo call")
            feedRepository.getTop10Feeds().collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Timber.d("has resource")
                        //return the value
                        if (resource.data.isNullOrEmpty())
                            mutableTrendingFeeds.value = StatefulResource.loading()
                        else
                            mutableTrendingFeeds.value = StatefulResource.success(resource)
                    }
                    Status.SUCCESS -> {
                        mutableTrendingFeeds.value = StatefulResource.success(resource)
                    }
                    Status.ERROR -> {
                        when (resource.error) {
                            is ApiErrorException -> {
                                mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR_API)
                                        setMessage(R.string.service_error)
                                    }
                            }
                            is NetworkErrorException -> {
                                mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setMessage(R.string.no_network_connection)
                                        setState(StatefulResource.State.ERROR_NETWORK)
                                    }
                            }
                            else -> {
                                mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR)
                                        setMessage(R.string.unknown_error)
                                    }
                            }
                        }
                    }
                    else -> mutableTrendingFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }


    @ExperimentalCoroutinesApi
    fun getSources() {
        viewModelScope.launch {
            Timber.d("Triggered sources stream call")
            sourceAndCategoryRepository.getSources().collect { data ->
                mutableSources.value = data
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getCategories() {
        viewModelScope.launch {
            Timber.d("Triggered categories stream call")
            sourceAndCategoryRepository.getCategories()
                .collect { data ->
                    mutableCategories.value = data
                }
        }
    }

    @ExperimentalCoroutinesApi
    fun getTags() {
        viewModelScope.launch {
            Timber.d("Triggered tags repo call")
            feedRepository.getFeedTags().collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Timber.d("has resource")
                        //return the value
                        if (resource.data.isNullOrEmpty())
                            mutableTags.value = StatefulResource.loading()
                        else
                            mutableTags.value = StatefulResource.success(resource)
                    }
                    Status.SUCCESS -> {
                        mutableTags.value = StatefulResource.success(resource)
                    }
                    Status.ERROR -> {
                        when (resource.error) {
                            is ApiErrorException -> {
                                mutableTags.value = StatefulResource<List<TagDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR_API)
                                        setMessage(R.string.service_error)
                                    }
                            }
                            is NetworkErrorException -> {
                                mutableTags.value = StatefulResource<List<TagDTO>?>()
                                    .apply {
                                        setMessage(R.string.no_network_connection)
                                        setState(StatefulResource.State.ERROR_NETWORK)
                                    }
                            }
                            else -> {
                                mutableTags.value = StatefulResource<List<TagDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR)
                                        setMessage(R.string.unknown_error)
                                    }
                            }
                        }
                    }
                    else -> mutableTags.value = StatefulResource<List<TagDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }

}