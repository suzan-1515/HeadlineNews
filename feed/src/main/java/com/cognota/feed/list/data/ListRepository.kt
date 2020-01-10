package com.cognota.feed.list.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.data.local.entity.TagEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.data.mapper.*
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.model.TagResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    private val categoryDTOMapper: CategoryDTOMapper,
    private val tagResponseMapper: TagResponseMapper,
    private val tagDTOMapper: TagDTOMapper
) : BaseRepository(), ListDataContract.Repository {

    override suspend fun getLatestFeeds(): Deferred<Resource<List<FeedWithRelatedFeedDTO>?>> {
        val sourceResource = getFeedSources().await()
        val categoryResource = getFeedCategories().await()
        val dataFetchHelper =
            object :
                DataFetchHelper.NetworkFirstLocalFailover<List<FeedWithRelatedEntity>?, List<FeedWithRelatedFeedDTO>?>(
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
                        if (categoryResource.hasData()) {
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
                    }

                    return listOf()
                }

                override suspend fun operateOnDataPostFetch(data: List<FeedWithRelatedFeedDTO>?) {
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

    override suspend fun getTop10Feeds(): Deferred<Resource<List<FeedDTO>?>> {
        val sourceResource = getFeedSources().await()
        val categoryResource = getFeedCategories().await()
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
                    }

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
                    TimeUnit.MINUTES.toMinutes(5) //storing news information for 15ø min only.
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
                    }
                    val categoryEntity = sourceResponse.categories.map {
                        categoryResponseMapper.toEntity(it)
                    }
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

    override suspend fun getFeedSourcesReactive(): Flow<List<SourceDTO>?> {
        return newsDao.findSourcesReactive().map { data ->
            data?.map {
                sourceDTOMapper.toDTO(it)
            }
        }
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

    override suspend fun getFeedCategoriesReactive(): Flow<List<CategoryDTO>?> {
        return newsDao.findCategoriesReactive().map { data ->
            data?.map {
                categoryDTOMapper.toDTO(it)
            }
        }
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