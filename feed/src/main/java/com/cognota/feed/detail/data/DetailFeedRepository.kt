package com.cognota.feed.detail.data

import com.cognota.core.repository.BaseRepository
import com.cognota.core.vo.Resource
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DetailFeedRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val feedDTOMapper: FeedDTOMapper
) : BaseRepository(), DetailFeedDataContract.Repository {

    @ExperimentalCoroutinesApi
    override suspend fun getRelatedFeeds(
        parentId: String
    ): Flow<Resource<List<FeedDTO>?>> {
        return newsDao.findRelatedFeeds(parentId).filterNotNull()
            .map { data ->
                Resource.success(data.map {
                    feedDTOMapper.toDTO(it)
                })
            }.flowOn(ioDispatcher)
    }

    @ExperimentalCoroutinesApi
    override suspend fun getFeedDetail(feedId: String): Flow<Resource<FeedDTO?>> {
        return flow {
            newsDao.findFeed(feedId)?.let { data ->
                emit(Resource.success(feedDTOMapper.toDTO(data)))
            }
        }.flowOn(ioDispatcher)
    }
}