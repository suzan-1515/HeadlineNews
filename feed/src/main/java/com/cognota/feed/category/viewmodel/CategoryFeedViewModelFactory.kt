package com.cognota.feed.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.category.data.CategoryFeedDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CategoryFeedViewModelFactory @Inject constructor(
    private var repository: CategoryFeedDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryFeedViewModel(
            repository
        ) as T
    }
}