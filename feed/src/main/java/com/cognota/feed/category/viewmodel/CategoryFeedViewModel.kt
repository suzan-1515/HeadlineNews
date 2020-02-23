package com.cognota.feed.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.data.ApiErrorException
import com.cognota.core.data.NetworkErrorException
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.core.vo.Status
import com.cognota.feed.R
import com.cognota.feed.category.data.CategoryFeedDataContract
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.flow.collect
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
                feedRepository.getFeedsByCategory(it, page).collect { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                            //return the value
                            if (resource.data == null)
                                mutableFeeds.value = StatefulResource.loading()
                            else
                                mutableFeeds.value = StatefulResource.success(resource)
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
    }

    fun categoryCode(code: String) {
        mutableCategoryCode.value = code
    }

}