package com.cognota.feed.detail.data

import com.cognota.core.ui.StatefulResource
import com.cognota.feed.commons.domain.RelatedFeedDTO
import kotlinx.coroutines.flow.Flow

interface DetailFeedDataContract {
    interface Repository {

        suspend fun getRelatedFeeds(parentId: String): Flow<StatefulResource<List<RelatedFeedDTO>?>>

    }
}