package com.cognota.feed.detail.data

import com.cognota.core.vo.Resource
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.flow.Flow

interface DetailFeedDataContract {
    interface Repository {

        suspend fun getRelatedFeeds(parentId: String): Flow<Resource<List<FeedDTO>?>>
        suspend fun getFeedDetail(feedId: String): Flow<Resource<FeedDTO?>>

    }
}