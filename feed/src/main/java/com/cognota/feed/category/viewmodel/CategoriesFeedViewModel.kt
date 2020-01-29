package com.cognota.feed.category.viewmodel

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
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoriesFeedViewModel(
    private val feedRepository: SourceAndCategoryDataContract.Repository
) : BaseViewModel() {

    private val mutableCategories: MutableLiveData<StatefulResource<SourceAndCategoryDTO?>> =
        MutableLiveData()
    val categories: LiveData<StatefulResource<SourceAndCategoryDTO?>> =
        mutableCategories

    private val mutableSelectedCategory: MutableLiveData<Int> = MutableLiveData()
    val selectedCategory: LiveData<Int> = mutableSelectedCategory

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            Timber.d("Triggered categories repo call")
            feedRepository.getSourcesAndCategories().collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        if (resource.data == null)
                            mutableCategories.value = StatefulResource.loading()
                        else
                            mutableCategories.value = StatefulResource.success(resource)
                    }
                    Status.SUCCESS -> {
                        mutableCategories.value = StatefulResource.success(resource)
                    }
                    Status.ERROR -> {
                        when (resource.error) {
                            is ApiErrorException -> {
                                mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR_API)
                                        setMessage(R.string.service_error)
                                    }
                            }
                            is NetworkErrorException -> {
                                mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                                    .apply {
                                        setMessage(R.string.no_network_connection)
                                        setState(StatefulResource.State.ERROR_NETWORK)
                                    }
                            }
                            else -> {
                                mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                                    .apply {
                                        setState(StatefulResource.State.ERROR)
                                        setMessage(R.string.unknown_error)
                                    }
                            }
                        }
                    }
                    else -> mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                        .apply {
                            setState(StatefulResource.State.ERROR)
                            setMessage(R.string.unknown_error)
                        }
                }
            }
        }
    }

    fun currentCategory(position: Int) {
        mutableSelectedCategory.value = position
    }

}