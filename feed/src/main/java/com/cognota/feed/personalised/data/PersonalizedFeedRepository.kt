package com.cognota.feed.personalised.data

import android.content.SharedPreferences
import com.cognota.core.networking.NetworkBoundResource
import com.cognota.core.repository.BaseRepository
import com.cognota.core.util.RepositoryUtil
import com.cognota.core.vo.Resource
import com.cognota.core.vo.Status
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.mapper.TagDTOMapper
import com.cognota.feed.commons.data.mapper.TagResponseMapper
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.model.TagResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.FeedType
import com.cognota.feed.commons.domain.TagDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PersonalizedFeedRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sharedPreferences: SharedPreferences,
    private val sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository,
    private val feedResponseMapper: FeedResponseMapper,
    private val feedDTOMapper: FeedDTOMapper,
    private val tagResponseMapper: TagResponseMapper,
    private val tagDTOMapper: TagDTOMapper
) : BaseRepository(), PersonalizedFeedDataContract.Repository {

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun getLatestFeeds(): Flow<Resource<List<FeedDTO>?>> {
        return sourceAndCategoryRepository.getSourcesAndCategories().filter {
            Timber.d("getSourcesAndCategories filter")
            it.status == Status.SUCCESS
        }.flatMapLatest {
            Timber.d("getLatestFeeds")
            object : NetworkBoundResource<List<FeedDTO>?, NewsFeedResponse>() {
                override suspend fun saveNetworkResult(item: NewsFeedResponse) {
                    Timber.d("saveNetworkResult")
                    if (!item.feeds.isNullOrEmpty())
                        newsDao.deleteAllLatestFeed(FeedType.LATEST.toString())
                    item.feeds.map { feed ->
                        val parentEntity = feedResponseMapper.toEntity(
                            feed, FeedType.LATEST
                        )

                        val relatedEntities = feed.related?.map { related ->
                            feedResponseMapper.toEntity(
                                related,
                                parentEntity
                            )
                        }

                        newsDao.insert(parentEntity)
                        relatedEntities?.let {
                            newsDao.insert(it)
                        }
                    }
                }

                override fun shouldFetch(data: List<FeedDTO>?): Boolean {
                    return data.isNullOrEmpty() ||
                            RepositoryUtil.shouldFetch(
                                sharedPreferences,
                                "latest_feed",
                                "latest_feed",
                                TimeUnit.MINUTES.toMillis(2)
                            )
                }

                override fun loadFromDb(): Flow<List<FeedDTO>?> {
                    Timber.d("loadFromDb")
                    return flow {
                        emit(
                            newsDao.findLatestFeeds()?.distinct()?.map { feed ->
                                feedDTOMapper.toDTO(feed)
                            }
                        )
                    }
                }

                override suspend fun fetchFromNetwork(): Response<NewsFeedResponse> {
                    Timber.d("fetchFromNetwork")
                    return newsApiAPIService.getLatestFeeds()
                }

                override fun onFetchFailed() {
                    RepositoryUtil.resetCache(
                        sharedPreferences,
                        "latest_feed",
                        "latest_feed"
                    )
                }

            }.asFlow().flowOn(ioDispatcher)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun getTop10Feeds(): Flow<Resource<List<FeedDTO>?>> {
        return sourceAndCategoryRepository.getSourcesAndCategories().filter {
            Timber.d("getSourcesAndCategories filter")
            it.status == Status.SUCCESS
        }.flatMapLatest {
            Timber.d("getTop10Feeds")
            object : NetworkBoundResource<List<FeedDTO>?, NewsFeedResponse>() {
                override suspend fun saveNetworkResult(item: NewsFeedResponse) {
                    Timber.d("saveNetworkResult")
                    val obj = item.feeds.map { feed ->
                        feedResponseMapper.toEntity(
                            feed, FeedType.TOP
                        )
                    }

                    if (!obj.isNullOrEmpty()) {
                        newsDao.deleteAllTopFeed(FeedType.TOP.toString())
                    }
                    newsDao.insert(obj)
                }

                override fun shouldFetch(data: List<FeedDTO>?): Boolean {
                    return data.isNullOrEmpty() ||
                            RepositoryUtil.shouldFetch(
                                sharedPreferences,
                                "top5_feed",
                                "top5_feed",
                                TimeUnit.MINUTES.toMillis(2)
                            )
                }

                override fun loadFromDb(): Flow<List<FeedDTO>?> {
                    Timber.d("loadFromDb")
                    return flow {
                        emit(
                            newsDao.findTop10Feeds()?.distinct()?.map { data ->
                                feedDTOMapper.toDTO(data)
                            }
                        )
                    }
                }

                override suspend fun fetchFromNetwork(): Response<NewsFeedResponse> {
                    Timber.d("fetchFromNetwork")
                    return newsApiAPIService.getTopFeeds()
                }

                override fun onFetchFailed() {
                    RepositoryUtil.resetCache(
                        sharedPreferences,
                        "top5_feed",
                        "top5_feed"
                    )
                }

            }.asFlow().flowOn(ioDispatcher)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun getFeedTags(): Flow<Resource<List<TagDTO>?>> {
        return object : NetworkBoundResource<List<TagDTO>?, TagResponse>() {
            override suspend fun saveNetworkResult(item: TagResponse) {
                Timber.d("saveNetworkResult")
                val tagEntities = item.tags.map {
                    tagResponseMapper.toEntity(it)
                }
                if (!tagEntities.isNullOrEmpty())
                    newsDao.deleteAllTags()
                newsDao.insertTags(tagEntities)

            }

            override fun shouldFetch(data: List<TagDTO>?): Boolean {
                return data.isNullOrEmpty() ||
                        RepositoryUtil.shouldFetch(
                            sharedPreferences,
                            "feed_tags",
                            "feed_tags",
                            TimeUnit.MINUTES.toMillis(2)
                        )
            }

            override fun loadFromDb(): Flow<List<TagDTO>?> {
                Timber.d("loadFromDb")
                return flow {
                    emit(
                        newsDao.findTags()?.distinct()?.map {
                            tagDTOMapper.toDTO(it)
                        }
                    )
                }
            }

            override suspend fun fetchFromNetwork(): Response<TagResponse> {
                Timber.d("fetchFromNetwork")
                return newsApiAPIService.getTags()
            }

            override fun onFetchFailed() {
                RepositoryUtil.resetCache(
                    sharedPreferences,
                    "feed_tags",
                    "feed_tags"
                )
            }

        }.asFlow().flowOn(ioDispatcher)

    }

}