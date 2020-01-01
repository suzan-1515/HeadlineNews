package com.cognota.feed.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.list.data.ListDataContract
import kotlinx.coroutines.launch
import timber.log.Timber

class ListViewModel(
    private val feedRepository: ListDataContract.Repository
) : BaseViewModel() {

    private val mutableLatestFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val latestFeeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableLatestFeeds

    fun getLatestFeed() {
        launch {
            Timber.d("Triggered repo call")
            mutableLatestFeeds.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getLatestFeeds().await()
            when {
                resource.hasData() -> {
                    Timber.d("has data")
                    //return the value
                    mutableLatestFeeds.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.service_error)
                        }
                else -> mutableLatestFeeds.value = StatefulResource<List<FeedDTO>?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.feed_not_available)
                    }
            }
        }
    }

}