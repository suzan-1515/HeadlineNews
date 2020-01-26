package com.cognota.feed.option.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cognota.core.ui.BaseViewModel
import com.cognota.core.util.SingleLiveEvent
import com.cognota.feed.commons.data.BookmarkDataContract
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.option.data.OptionEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedOptionViewModel(
    private val bookmarkFeedRepository: BookmarkDataContract.Repository
) : BaseViewModel() {

    private val mutableOptionEvent: SingleLiveEvent<OptionEvent> = SingleLiveEvent()
    val optionEventLiveData: LiveData<OptionEvent> = mutableOptionEvent

    private val mutableBookmarkStatus: MutableLiveData<OptionEvent> = MutableLiveData()
    val bookmarkStatusLiveData: LiveData<OptionEvent> = mutableBookmarkStatus

    private val mutableLikeStatus: MutableLiveData<OptionEvent> = MutableLiveData()
    val likeStatusLiveData: LiveData<OptionEvent> = mutableLikeStatus

    fun bookmarkFeed(feed: FeedDTO) {
        viewModelScope.launch {
            bookmarkFeedRepository.bookmarkFeed(feed)
            mutableOptionEvent.value = OptionEvent.BOOKMARKED
        }
    }

    fun unBookmarkFeed(feed: FeedDTO) {
        viewModelScope.launch {
            bookmarkFeedRepository.removeFromBookmark(feed.id)
            mutableOptionEvent.value = OptionEvent.UNBOOKMARKED
        }
    }

    fun hideFeed(feed: FeedDTO) {
        viewModelScope.launch {
            //TODO Hide feed implementation
            mutableOptionEvent.value = OptionEvent.HIDDEN
        }
    }

    fun unHideFeed(feed: FeedDTO) {
        viewModelScope.launch {
            //TODO UnHide feed implementation
            mutableOptionEvent.value = OptionEvent.UNHIDDEN
        }
    }

    fun likeFeed(feed: FeedDTO) {
        viewModelScope.launch {
            //TODO Like feed implementation
            mutableOptionEvent.value = OptionEvent.LIKED
        }
    }

    fun dislikeFeed(feed: FeedDTO) {
        viewModelScope.launch {
            //TODO Dislike feed implementation
            mutableOptionEvent.value = OptionEvent.DISLIKED
        }
    }

    fun getBookmarkedStatus(id: String) {
        if (mutableBookmarkStatus.value == null) {
            viewModelScope.launch {
                bookmarkFeedRepository.getBookmarkedFeed(id).collect {
                    it?.let {
                        mutableBookmarkStatus.value = OptionEvent.BOOKMARKED
                    } ?: run {
                        mutableBookmarkStatus.value = OptionEvent.UNBOOKMARKED
                    }
                }
            }
        }
    }

    fun getLikeStatus(id: String) {
        if (mutableBookmarkStatus.value == null) {
            viewModelScope.launch {
                // TODO Like status implementation
                mutableLikeStatus.value = OptionEvent.LIKED
//                feedRepository.getBookmarkedFeed(id).collect {
//                    it?.let {
//                        mutableBookmarkStatus.value = OptionEvent.BOOKMARKED
//                    } ?: run {
//                        mutableBookmarkStatus.value = OptionEvent.UNBOOKMARKED
//                    }
//                }
            }
        }
    }


}