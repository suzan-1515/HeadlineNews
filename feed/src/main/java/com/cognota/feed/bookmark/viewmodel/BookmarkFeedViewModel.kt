package com.cognota.feed.bookmark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.util.SingleLiveEvent
import com.cognota.feed.commons.data.BookmarkDataContract
import com.cognota.feed.commons.domain.BookmarkDTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class BookmarkFeedViewModel(
    private val feedRepository: BookmarkDataContract.Repository
) : BaseViewModel() {

    private val mutableBookmarkFeeds: MutableLiveData<List<BookmarkDTO>?> =
        MutableLiveData()
    val bookmarkedFeedsLiveData: LiveData<List<BookmarkDTO>?> =
        mutableBookmarkFeeds

    private val mutableBookmarksClearedEvent: SingleLiveEvent<Unit> = SingleLiveEvent()
    val bookmarksClearedEventLiveData: LiveData<Unit> = mutableBookmarksClearedEvent

    fun getBookmarkFeeds() {
        if (bookmarkedFeedsLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                Timber.d("Triggered bookmarked stream call")
                feedRepository.getBookmarkedFeeds().collect { data ->
                    mutableBookmarkFeeds.value = data
                }
            }
        }
    }

    fun clearBookmarkFeeds() {
        viewModelScope.launch {
            feedRepository.clearBookmark()
            mutableBookmarksClearedEvent.value = Unit
        }
    }

}