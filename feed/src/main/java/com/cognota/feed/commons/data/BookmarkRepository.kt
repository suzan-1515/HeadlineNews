package com.cognota.feed.commons.data

import com.cognota.core.repository.BaseRepository
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.BookmarkDTOMapper
import com.cognota.feed.commons.domain.BookmarkDTO
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val bookmarkDTOMapper: BookmarkDTOMapper
) : BaseRepository(), BookmarkDataContract.Repository {

    override suspend fun getBookmarkedFeeds(): Flow<List<BookmarkDTO>?> {
        return newsDao.findBookmarkedFeeds().map { data ->
            data?.map {
                bookmarkDTOMapper.toDTO(it)
            }
        }
    }

    override suspend fun removeFromBookmark(id: String): Boolean {
        withContext(Dispatchers.IO) {
            newsDao.deleteBookmarkFeed(id)
        }
        return true
    }

    override suspend fun clearBookmark(): Boolean {
        withContext(Dispatchers.IO) {
            newsDao.deleteAllBookmarkFeed()
        }
        return true
    }

    override suspend fun bookmarkFeed(feed: FeedDTO) {
        withContext(Dispatchers.IO) {
            newsDao.insertBookmarkFeed(bookmarkDTOMapper.fromFeedDto(feed))
        }
    }
}