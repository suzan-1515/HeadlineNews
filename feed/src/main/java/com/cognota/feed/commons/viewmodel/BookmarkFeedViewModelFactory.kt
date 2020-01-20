package com.cognota.feed.commons.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognota.feed.commons.data.BookmarkDataContract
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class BookmarkFeedViewModelFactory @Inject constructor(
    private var repository: BookmarkDataContract.Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookmarkFeedViewModel(repository) as T
    }
}