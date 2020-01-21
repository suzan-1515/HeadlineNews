package com.cognota.feed.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.commons.domain.RelatedFeedDTO
import com.cognota.feed.detail.data.DetailFeedDataContract
import com.cognota.feed.detail.ui.DetailFeedFragmentArgs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFeedViewModel(
    private val feedRepository: DetailFeedDataContract.Repository
) : BaseViewModel() {

    private val mutableRelatedFeeds: MutableLiveData<StatefulResource<List<RelatedFeedDTO>?>> =
        MutableLiveData()
    val relatedFeeds: LiveData<StatefulResource<List<RelatedFeedDTO>?>> =
        mutableRelatedFeeds

    private val mutableArgs: MutableLiveData<DetailFeedFragmentArgs> =
        MutableLiveData()

    fun getRelatedFeed(parentId: String) {
        viewModelScope.launch {
            Timber.d("Triggered related feed repo call: %s", parentId)
            feedRepository.getRelatedFeeds(parentId).collect {
                mutableRelatedFeeds.value = it
            }
        }
    }


}