package com.cognota.feed.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.list.data.SourceAndCategoryDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CategoriesFeedViewModelFactory @Inject constructor(
    private var repository: SourceAndCategoryDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesFeedViewModel(repository) as T
    }
}