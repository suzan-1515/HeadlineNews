package com.cognota.feed.list.data

import com.cognota.core.repository.Resource
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.Deferred

interface CategoryFeedDataContract {
    interface Repository {

        suspend fun getFeedsByCategory(
            category: String,
            page: Int
        ): Deferred<Resource<List<FeedDTO>?>>
    }
}