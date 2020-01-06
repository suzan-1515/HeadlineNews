package com.cognota.feed.list.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.data.mapper.*
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sharedPreferences: SharedPreferences,
    private val feedResponseMapper: FeedResponseMapper,
    private val feedDTOMapper: FeedDTOMapper,
    private val sourceResponseMapper: SourceResponseMapper,
    private val sourceDTOMapper: SourceDTOMapper,
    private val categoryResponseMapper: CategoryResponseMapper,
    private val categoryDTOMapper: CategoryDTOMapper
) : BaseRepository(), ListDataContract.Repository {

    override suspend fun getLatestFeeds(): Deferred<Resource<PersonalisedFeedDTO>> {
        val sourceResource = getFeedSources().await()
        val categoryResource = getFeedCategories().await()
        val dataFetchHelper =
            object :
                DataFetchHelper.NetworkFirstLocalFailover<List<FeedWithRelatedEntity>?, PersonalisedFeedDTO>(
                    "latest_feed"
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
                        }?.toMutableList()

                        newsDao.insert(parentEntity)
                        relatedEntities?.let {
                            newsDao.insertRelatedFeeds(it)
                        }
                    }

                    return true
                }

                override suspend fun convertDataToDto(data: List<FeedWithRelatedEntity>?): PersonalisedFeedDTO {
                    if (sourceResource.hasData()) {
                        if (categoryResource.hasData()) {
                            val feedWithRelatedFeeds = data?.map { fwre ->
                                val relatedFeedDtoList = fwre.relatedFeeds?.map {
                                    feedDTOMapper.toDTO(it)
                                }?.toMutableList()
                                FeedWithRelatedFeedDTO(
                                    feed = feedDTOMapper.toDTO(fwre.feed),
                                    feedWithRelatedFeeds = if (relatedFeedDtoList.isNullOrEmpty()) listOf() else relatedFeedDtoList
                                )
                            }?.toMutableList()

                            return PersonalisedFeedDTO(
                                feeds = feedWithRelatedFeeds,
                                sources = sourceResource.data,
                                categories = categoryResource.data
                            )
                        }
                    }

                    return PersonalisedFeedDTO(
                        feeds = listOf(),
                        sources = listOf(),
                        categories = listOf()
                    )
                }

                override suspend fun operateOnDataPostFetch(data: PersonalisedFeedDTO) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getTopFeeds(): Deferred<Resource<List<FeedDTO>?>> {
        val sourceResource = getFeedSources().await()
        val categoryResource = getFeedCategories().await()
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<FeedWithSourcesEntity>?, List<FeedDTO>?>(
                    "top_feed",
                    sharedPreferences,
                    "top_feed",
                    "top_feed",
                    TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getTopFeeds()
                }

                override suspend fun getDataFromLocal(): List<FeedWithSourcesEntity>? {
                    return newsDao.findTopFeeds()
                }

                override suspend fun convertDataToDto(data: List<FeedWithSourcesEntity>?): List<FeedDTO>? {
                    if (sourceResource.hasData()) {
                        if (categoryResource.hasData()) {
                            return data?.map {
                                feedDTOMapper.toDTO(it)
                            }
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
                    }.toMutableList()

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

    override suspend fun getFeedsByCategory(
        category: String,
        page: Int
    ): Deferred<Resource<List<FeedDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<FeedWithSourcesEntity>?, List<FeedDTO>?>(
                    "category_feed",
                    sharedPreferences,
                    "category_feed",
                    category,
                    TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getFeedsByCategory(page, category)
                }

                override suspend fun getDataFromLocal(): List<FeedWithSourcesEntity>? {
                    return newsDao.findFeedsByCategory(category)
                }

                override suspend fun convertDataToDto(data: List<FeedWithSourcesEntity>?): List<FeedDTO>? {
                    return data?.let { list ->
                        list.map {
                            feedDTOMapper.toDTO(it)
                        }
                    }
                }

                override suspend fun storeFreshRawDataToLocal(response: Response<out Any?>): Boolean {
                    val newsResponse = response.body() as NewsFeedResponse
                    val obj = newsResponse.feeds.map { feed ->
                        feedResponseMapper.toEntity(
                            feed, FeedType.CATEGORY
                        )
                    }.toMutableList()

                    if (!obj.isNullOrEmpty()) {
                        newsDao.deleteAllFeedByCategory(category)
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

    override suspend fun getFeedSources(): Deferred<Resource<List<SourceDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<SourceEntity>?, List<SourceDTO>?>(
                    "feed_sources",
                    sharedPreferences,
                    "feed_sources",
                    "feed_sources",
                    TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getSources()
                }

                override suspend fun getDataFromLocal(): List<SourceEntity>? {
                    return newsDao.findSources()
                }

                override suspend fun convertDataToDto(data: List<SourceEntity>?): List<SourceDTO>? {
                    return data?.map {
                        sourceDTOMapper.toDTO(it)
                    }

                }

                override suspend fun storeFreshRawDataToLocal(response: Response<out Any?>): Boolean {
                    val sourceResponse = response.body() as SourceResponse
                    val sourcesEntity = sourceResponse.sources.map {
                        sourceResponseMapper.toEntity(it)
                    }.toMutableList()
                    val categoryEntity = sourceResponse.categories.map {
                        categoryResponseMapper.toEntity(it)
                    }.toMutableList()
                    if (!sourcesEntity.isNullOrEmpty())
                        newsDao.deleteAllSources()
                    if (!categoryEntity.isNullOrEmpty())
                        newsDao.deleteAllCategories()
                    newsDao.insertSources(sourcesEntity)
                    newsDao.insertCategories(categoryEntity)

                    return true
                }

                override suspend fun operateOnDataPostFetch(data: List<SourceDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getFeedCategories(): Deferred<Resource<List<CategoryDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalOnly<List<CategoryEntity>?, List<CategoryDTO>?>(
                    "feed_categories"
                ) {
                override suspend fun getDataFromLocal(): List<CategoryEntity>? {
                    return newsDao.findCategories()
                }

                override suspend fun convertDataToDto(data: List<CategoryEntity>?): List<CategoryDTO>? {
                    return data?.map {
                        categoryDTOMapper.toDTO(it)
                    }
                }

            }

        return dataFetchHelper.fetchDataIOAsync()
    }

}