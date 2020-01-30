package com.cognota.feed.search.data

import com.cognota.core.vo.Resource
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import kotlinx.coroutines.flow.Flow

interface SearchFeedDataContract {
    interface Repository {

        suspend fun getFeeds(query: String): Flow<Resource<List<FeedDTO>?>>
        suspend fun getTags(): Flow<Resource<List<TagDTO>?>>
        suspend fun getHistory(): Flow<Resource<List<TagDTO>?>>
    }
}