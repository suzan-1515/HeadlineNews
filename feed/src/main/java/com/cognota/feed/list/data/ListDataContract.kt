package com.cognota.feed.list.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.Deferred

interface ListDataContract {
    interface Repository {

        suspend fun getLatestFeeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getTopFeeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedsByCategory(category: String): Deferred<Resource<List<FeedDTO>?>>
    }
}