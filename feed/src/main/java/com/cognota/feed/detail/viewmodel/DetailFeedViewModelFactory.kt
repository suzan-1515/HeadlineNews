package com.cognota.feed.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.detail.data.DetailFeedDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DetailFeedViewModelFactory @Inject constructor(
    private var repository: DetailFeedDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailFeedViewModel(repository) as T
    }
}