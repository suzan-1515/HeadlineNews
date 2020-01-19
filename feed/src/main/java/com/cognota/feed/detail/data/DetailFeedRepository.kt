package com.cognota.feed.detail.data

import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.core.ui.StatefulResource
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.domain.RelatedFeedDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailFeedRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val feedDTOMapper: FeedDTOMapper
) : BaseRepository(), DetailFeedDataContract.Repository {

    override suspend fun getRelatedFeeds(
        parentId: String
    ): Flow<StatefulResource<List<RelatedFeedDTO>?>> {
        return flow {
            emit(StatefulResource.with(StatefulResource.State.LOADING))
            val data = newsDao.findRelatedFeeds(parentId)?.map { data ->
                feedDTOMapper.toDTO(data)
            }
            val resource = Resource<List<RelatedFeedDTO>?>().apply {
                this.data = data
                dataFetchStyle = DataFetchHelper.DataFetchStyle.LOCAL_ONLY
                dataFetchStyleResult =
                    DataFetchHelper.DataFetchStyle.Result.LOCAL_DATA_ONLY
            }
            emit(
                StatefulResource.success(resource)
            )
        }.flowOn(Dispatchers.IO)
    }
}