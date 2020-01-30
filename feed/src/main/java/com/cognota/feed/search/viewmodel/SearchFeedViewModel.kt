package com.cognota.feed.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.data.model.api.ApiErrorException
import com.cognota.core.data.model.api.NetworkErrorException
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.core.vo.Status
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import com.cognota.feed.search.data.SearchFeedDataContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchFeedViewModel(
    private val feedRepository: SearchFeedDataContract.Repository
) : BaseViewModel() {

    private val mutableFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val feeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableFeeds

    private val mutableTags: MutableLiveData<List<TagDTO>?> =
        MutableLiveData()
    val tags: LiveData<List<TagDTO>?> =
        mutableTags

    init {
        getTags()
    }

    @ExperimentalCoroutinesApi
    fun getSearchFeed(query: String) {
        viewModelScope.launch {
            Timber.d("Triggered getSearchFeed repo call")
            feedRepository.getFeeds(query).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        mutableFeeds.value = StatefulResource.loading()
                    }
                    Status.SUCCESS -> {
                        mutableFeeds.value = StatefulResource.success(resource)
                    }
                    Status.ERROR -> {
                        when (resource.error) {
                            is ApiErrorException -> {
                                mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR_API)
                                        setMessage(R.string.service_error)
                                    }
                            }
                            is NetworkErrorException -> {
                                mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setMessage(R.string.no_network_connection)
                                        setState(StatefulResource.State.ERROR_NETWORK)
                                    }
                            }
                            else -> {
                                mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR)
                                        setMessage(R.string.unknown_error)
                                    }
                            }
                        }
                    }
                    else -> mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getTags() {
        viewModelScope.launch {
            Timber.d("Triggered tags stream call")
            feedRepository.getTags()
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            mutableTags.value = resource.data
                        }
                        Status.LOADING -> {

                        }
                        else -> {
                        }
                    }

                }
        }
    }

}