package com.cognota.feed.personalised.data

import com.cognota.core.vo.Resource
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import kotlinx.coroutines.flow.Flow

interface PersonalizedFeedDataContract {
    interface Repository {

        suspend fun getLatestFeeds(): Flow<Resource<List<FeedDTO>?>>

        suspend fun getTop10Feeds(): Flow<Resource<List<FeedDTO>?>>

        suspend fun getFeedTags(): Flow<Resource<List<TagDTO>?>>
    }
}