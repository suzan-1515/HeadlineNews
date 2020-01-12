package com.cognota.feed.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.list.data.CategoryFeedDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CategoryFeedViewModelFactory @Inject constructor(
    private var repository: CategoryFeedDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryFeedViewModel(repository) as T
    }
}