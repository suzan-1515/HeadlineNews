package com.cognota.feed.bookmark.data

import com.cognota.feed.commons.domain.BookmarkDTO
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.flow.Flow

interface BookmarkDataContract {
    interface Repository {

        suspend fun getBookmarkedFeeds(): Flow<List<BookmarkDTO>?>

        suspend fun getBookmarkedFeed(id: String): Flow<BookmarkDTO?>

        suspend fun removeFromBookmark(id: String): Boolean

        suspend fun clearBookmark(): Boolean

        suspend fun bookmarkFeed(feed: FeedDTO)

    }
}