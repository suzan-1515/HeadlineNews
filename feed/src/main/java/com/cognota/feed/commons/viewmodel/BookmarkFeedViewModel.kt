package com.cognota.feed.commons.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.feed.commons.data.BookmarkDataContract
import com.cognota.feed.commons.domain.BookmarkDTO
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class BookmarkFeedViewModel(
    private val feedRepository: BookmarkDataContract.Repository
) : BaseViewModel() {

    private val mutableBookmarkFeeds: MutableLiveData<List<BookmarkDTO>?> =
        MutableLiveData()
    val bookmarkFeeds: LiveData<List<BookmarkDTO>?> =
        mutableBookmarkFeeds

    init {
        getBookmarkFeeds()
    }

    fun getBookmarkFeeds() {
        viewModelScope.launch {
            Timber.d("Triggered bookmarked stream call")
            feedRepository.getBookmarkedFeeds().collect { data ->
                mutableBookmarkFeeds.value = data
            }
        }
    }

    fun clearBookmarkFeeds() {
        viewModelScope.launch {
            feedRepository.clearBookmark()
        }
    }

    fun bookmarkFeed(it: FeedDTO) {
        viewModelScope.launch {
            feedRepository.bookmarkFeed(it)
        }
    }


}