package com.cognota.feed.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.domain.PersonalisedFeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import com.cognota.feed.list.data.ListDataContract
import kotlinx.coroutines.launch
import timber.log.Timber

class ListViewModel(
    private val feedRepository: ListDataContract.Repository
) : BaseViewModel() {

    private val mutableLatestFeeds: MutableLiveData<StatefulResource<PersonalisedFeedDTO>> =
        MutableLiveData()
    val latestFeeds: LiveData<StatefulResource<PersonalisedFeedDTO>> =
        mutableLatestFeeds

    private val mutableSources: MutableLiveData<StatefulResource<List<SourceDTO>?>> =
        MutableLiveData()
    val sources: LiveData<StatefulResource<List<SourceDTO>?>> =
        mutableSources


    fun getLatestFeed() {
        launch {
            Timber.d("Triggered feed repo call")
            mutableLatestFeeds.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getLatestFeeds().await()
            when {
                resource.hasData() -> {
                    Timber.d("has data")
                    //return the value
                    mutableLatestFeeds.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableLatestFeeds.value = StatefulResource<PersonalisedFeedDTO>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableLatestFeeds.value = StatefulResource<PersonalisedFeedDTO>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.service_error)
                        }
                else -> mutableLatestFeeds.value = StatefulResource<PersonalisedFeedDTO>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.feed_not_available)
                    }
            }
        }
    }

    fun getSources() {
        launch {
            Timber.d("Triggered sources repo call")
            mutableSources.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getFeedSources().await()
            when {
                resource.hasData() -> {
                    Timber.d("has data")
                    //return the value
                    mutableSources.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableSources.value = StatefulResource<List<SourceDTO>?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableSources.value = StatefulResource<List<SourceDTO>?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.error_fetching_sources)
                        }
                else -> mutableSources.value = StatefulResource<List<SourceDTO>?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.sources_not_available)
                    }
            }
        }
    }

}