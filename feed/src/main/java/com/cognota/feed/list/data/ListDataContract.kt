package com.cognota.feed.list.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface ListDataContract {
    interface Repository {

        suspend fun getLatestFeeds(): Deferred<Resource<List<FeedWithRelatedFeedDTO>?>>

        suspend fun getTopFeeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getTop10Feeds(): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedsByCategory(
            category: String,
            page: Int
        ): Deferred<Resource<List<FeedDTO>?>>

        suspend fun getFeedSources(): Deferred<Resource<List<SourceDTO>?>>

        suspend fun getFeedSourcesReactive(): Flow<List<SourceDTO>?>

        suspend fun getFeedCategories(): Deferred<Resource<List<CategoryDTO>?>>

        suspend fun getFeedCategoriesReactive(): Flow<List<CategoryDTO>?>

        suspend fun getFeedTags(): Deferred<Resource<List<TagDTO>?>>
    }
}