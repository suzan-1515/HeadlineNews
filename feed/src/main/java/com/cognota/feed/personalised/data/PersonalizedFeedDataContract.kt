package com.cognota.feed.personalised.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.TagDTO
import kotlinx.coroutines.Deferred

interface PersonalizedFeedDataContract {
    interface Repository {

        suspend fun getLatestFeeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getTop10Feeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedTags(): Deferred<Resource<List<TagDTO>?>>
    }
}