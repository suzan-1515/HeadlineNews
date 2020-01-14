package com.cognota.feed.personalised.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.TagEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.mapper.TagDTOMapper
import com.cognota.feed.commons.data.mapper.TagResponseMapper
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.model.TagResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.FeedWithRelatedFeedDTO
import com.cognota.feed.commons.domain.TagDTO
import kotlinx.coroutines.Deferred
import retrofit2.Response
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

    override suspend fun getLatestFeeds(): Deferred<Resource<List<FeedWithRelatedFeedDTO>?>> {
        val sourceResource = sourceAndCategoryRepository.getSourcesAndCategories().await()
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<FeedWithRelatedEntity>?, List<FeedWithRelatedFeedDTO>?>(
                    "latest_feed",
                    sharedPreferences,
                    "latest_feed",
                    "latest_feed",
                    TimeUnit.MINUTES.toMinutes(1)
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getLatestFeeds()
                }

                override suspend fun getDataFromLocal(): List<FeedWithRelatedEntity>? {
                    return newsDao.findLatestFeeds()
                }

                override suspend fun storeFreshRawDataToLocal(response: Response<out Any?>): Boolean {
                    val newsResponse = response.body() as NewsFeedResponse
                    if (!newsResponse.feeds.isNullOrEmpty()) newsDao.deleteAllLatestFeed(FeedType.LATEST.toString())
                    newsResponse.feeds.map { feed ->
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
                            newsDao.insertRelatedFeeds(it)
                        }
                    }

                    return true
                }

                override suspend fun convertDataToDto(data: List<FeedWithRelatedEntity>?): List<FeedWithRelatedFeedDTO>? {
                    if (sourceResource.hasData()) {
                        return data?.map { fwre ->
                            val relatedFeedDtoList = fwre.relatedFeeds?.map {
                                feedDTOMapper.toDTO(it)
                            }
                            FeedWithRelatedFeedDTO(
                                feed = feedDTOMapper.toDTO(fwre.feed),
                                feedWithRelatedFeeds = if (relatedFeedDtoList.isNullOrEmpty()) listOf() else relatedFeedDtoList
                            )
                        }

                    }

                    return listOf()
                }

                override suspend fun operateOnDataPostFetch(data: List<FeedWithRelatedFeedDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getTop10Feeds(): Deferred<Resource<List<FeedDTO>?>> {
        val sourceResource = sourceAndCategoryRepository.getSourcesAndCategories().await()
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<FeedWithSourcesEntity>?, List<FeedDTO>?>(
                    "top5_feed",
                    sharedPreferences,
                    "top5_feed",
                    "top5_feed",
                    TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getTopFeeds()
                }

                override suspend fun getDataFromLocal(): List<FeedWithSourcesEntity>? {
                    return newsDao.findTop10Feeds()
                }

                override suspend fun convertDataToDto(data: List<FeedWithSourcesEntity>?): List<FeedDTO>? {
                    if (sourceResource.hasData()) {
                        return data?.map {
                            feedDTOMapper.toDTO(it)
                        }
                    }

                    return listOf()
                }

                override suspend fun storeFreshRawDataToLocal(response: Response<out Any?>): Boolean {
                    val newsResponse = response.body() as NewsFeedResponse
                    val obj = newsResponse.feeds.map { feed ->
                        feedResponseMapper.toEntity(
                            feed, FeedType.TOP
                        )
                    }

                    if (!obj.isNullOrEmpty()) {
                        newsDao.deleteAllTopFeed(FeedType.TOP.toString())
                    }

                    newsDao.insert(obj)

                    return true
                }

                override suspend fun operateOnDataPostFetch(data: List<FeedDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getFeedTags(): Deferred<Resource<List<TagDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<TagEntity>?, List<TagDTO>?>(
                    "feed_tags",
                    sharedPreferences,
                    "feed_tags",
                    "feed_tags",
                    TimeUnit.MINUTES.toMinutes(5) //storing news information for 5ø min only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getTags()
                }

                override suspend fun getDataFromLocal(): List<TagEntity>? {
                    return newsDao.findTags()
                }

                override suspend fun convertDataToDto(data: List<TagEntity>?): List<TagDTO>? {
                    return data?.map {
                        tagDTOMapper.toDTO(it)
                    }

                }

                override suspend fun storeFreshRawDataToLocal(response: Response<out Any?>): Boolean {
                    val tagResponse = response.body() as TagResponse
                    val tagEntities = tagResponse.tags.map {
                        tagResponseMapper.toEntity(it)
                    }
                    if (!tagEntities.isNullOrEmpty())
                        newsDao.deleteAllTags()
                    newsDao.insertTags(tagEntities)

                    return true
                }

                override suspend fun operateOnDataPostFetch(data: List<TagDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

}