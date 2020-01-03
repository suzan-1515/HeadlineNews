package com.cognota.feed.list.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.PersonalisedFeedDTO
import com.cognota.feed.commons.domain.SourceDTO
import kotlinx.coroutines.Deferred

interface ListDataContract {
    interface Repository {

        suspend fun getLatestFeeds(): Deferred<Resource<PersonalisedFeedDTO>>

        suspend fun getTopFeeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedsByCategory(
            category: String,
            page: Int
        ): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedSources(): Deferred<Resource<List<SourceDTO>?>>

        suspend fun getFeedCategories(): Deferred<Resource<List<CategoryDTO>?>>
    }
}