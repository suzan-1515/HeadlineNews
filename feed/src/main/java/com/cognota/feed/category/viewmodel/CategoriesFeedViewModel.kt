package com.cognota.feed.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.R
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
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
            mutableCategories.value = StatefulResource.with(StatefulResource.State.LOADING)
            val resource = feedRepository.getSourcesAndCategories().await()
            when {
                resource.hasData() -> {
                    //return the value
                    mutableCategories.value = StatefulResource.success(resource)
                }
                resource.isNetworkIssue() -> {
                    mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                        .apply {
                            setMessage(R.string.no_network_connection)
                            setState(StatefulResource.State.ERROR_NETWORK)
                        }
                }
                resource.isApiIssue() -> //TODO 4xx isn't necessarily a service error, expand this to sniff http code before saying service error
                    mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                        .apply {
                            setState(StatefulResource.State.ERROR_API)
                            setMessage(R.string.service_error)
                        }
                else -> mutableCategories.value = StatefulResource<SourceAndCategoryDTO?>()
                    .apply {
                        setState(StatefulResource.State.SUCCESS)
                        setMessage(R.string.unknown_error)
                    }
            }
        }
    }

    fun currentCategory(position: Int) {
        mutableSelectedCategory.value = position
    }

}