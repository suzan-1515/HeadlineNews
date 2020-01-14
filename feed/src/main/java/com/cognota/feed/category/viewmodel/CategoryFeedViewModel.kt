package com.cognota.feed.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.category.data.CategoryFeedDataContract
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoryFeedViewModel(
    private val feedRepository: CategoryFeedDataContract.Repository
) : BaseViewModel() {

    private val mutableFeeds: MutableLiveData<StatefulResource<List<FeedDTO>?>> =
        MutableLiveData()
    val feeds: LiveData<StatefulResource<List<FeedDTO>?>> =
        mutableFeeds

    private val mutableCategoryCode: MutableLiveData<String> = MutableLiveData()
    val categoryCode: LiveData<String> = mutableCategoryCode

    fun getFeeds(page: Int) {
        categoryCode.value?.let {
            viewModelScope.launch {
                Timber.d("Triggered category feed repo call: %s", it)
                mutableFeeds.value = StatefulResource.with(StatefulResource.State.LOADING)
                val resource = feedRepository.getFeedsByCategory(it, page).await()
                when {
                    resource.hasData() -> {
                        //return the value
                        mutableFeeds.value = StatefulResource.success(resource)
                    }
                    resource.isNetworkIssue() -> {
                        mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                            .apply {
                                setMessage(R.string.no_network_connection)
                                setState(StatefulResource.State.ERROR_NETWORK)
                            }
                    }
                    resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                        mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                            .apply {
                                setState(StatefulResource.State.ERROR_API)
                                setMessage(R.string.service_error)
                            }
                    else -> mutableFeeds.value = StatefulResource<List<FeedDTO>?>()
                        .apply {
                            setState(StatefulResource.State.SUCCESS)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }

    fun categoryCode(code: String) {
        mutableCategoryCode.value = code
    }

}