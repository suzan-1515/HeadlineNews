package com.cognota.feed.category.data

import com.cognota.core.vo.Resource
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.flow.Flow

interface CategoryFeedDataContract {
    interface Repository {

        suspend fun getFeedsByCategory(
            category: String,
            page: Int
        ): Flow<Resource<List<FeedDTO>?>>
    }
}