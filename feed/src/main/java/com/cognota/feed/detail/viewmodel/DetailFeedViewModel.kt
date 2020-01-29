package com.cognota.feed.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.core.vo.Status
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.detail.data.DetailFeedDataContract
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFeedViewModel(
    private val feedRepository: DetailFeedDataContract.Repository
) : BaseViewModel() {

    private val mutableRelatedFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val relatedFeeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableRelatedFeeds

    private val mutableFeedDetail: MutableLiveData<StatefulResource<FeedDTO?>> =
        MutableLiveData()
    val feedDetail: LiveData<StatefulResource<FeedDTO?>> =
        mutableFeedDetail

    fun getRelatedFeed(parentId: String) {
        viewModelScope.launch {
            Timber.d("Triggered related feed repo call: %s", parentId)
            feedRepository.getRelatedFeeds(parentId).collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        mutableRelatedFeeds.value = StatefulResource.loading()
                    }
                    Status.SUCCESS -> {
                        mutableRelatedFeeds.value = StatefulResource.success(resource)
                    }
                    else -> {
                        mutableRelatedFeeds.value = StatefulResource<List<FeedDTO>?>()
                            .apply {
                                setState(StatefulResource.State.ERROR)
                                setMessage(R.string.unknown_error)
                            }
                    }
                }
            }
        }
    }

    fun getFeedDetail(feedId: String) {
        viewModelScope.launch {
            Timber.d("Triggered feed detail repo call: %s", feedId)
            feedRepository.getFeedDetail(feedId).collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        mutableFeedDetail.value = StatefulResource.loading()
                    }
                    Status.SUCCESS -> {
                        mutableFeedDetail.value = StatefulResource.success(resource)
                    }
                    else -> {
                        mutableFeedDetail.value = StatefulResource<FeedDTO?>()
                            .apply {
                                setState(StatefulResource.State.ERROR)
                                setMessage(R.string.unknown_error)
                            }
                    }
                }
            }
        }
    }


}