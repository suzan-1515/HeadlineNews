package com.cognota.feed.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.search.data.SearchFeedDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class SearchFeedViewModelFactory @Inject constructor(
    private var repository: SearchFeedDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchFeedViewModel(repository) as T
    }
}