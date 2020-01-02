package com.cognota.feed.list.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity
import com.cognota.feed.commons.data.local.relation.FeedWithRelatedEntity
import com.cognota.feed.commons.data.mapper.*
import com.cognota.feed.commons.data.remote.model.NewsResponse
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.SourceDTO
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

    override suspend fun getLatestFeeds(): Deferred<Resource<List<FeedDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.NetworkFirstLocalFailover<List<FeedWithRelatedEntity>?, List<FeedDTO>?>(
                    "latest_feed"
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getLatestFeeds()
                }

                override suspend fun getDataFromLocal(): List<FeedWithRelatedEntity>? {
                    return newsDao.findLatestFeeds()
                }

                override suspend fun convertApiResponseToData(response: Response<out Any?>): List<FeedWithRelatedEntity>? {
                    val newsResponse = response.body() as NewsResponse
                    return newsResponse.feedResponses.map {
                        feedResponseMapper.toWithRelatedEntities(
                            it, FeedType.LATEST
                        )
                    }
                }

                override suspend fun convertDataToDto(data: List<FeedWithRelatedEntity>?): List<FeedDTO>? {
                    return data?.let { list ->
                        list.map {
                            feedDTOMapper.toDTO(it).apply {
                                relatedFeed?.let { related ->
                                    related.map { feed ->
                                        feed.source?.let { source ->
                                            newsDao.findSourceByCode(source)?.let { entity ->
                                                feed.sourceDTO = sourceDTOMapper.toDTO(entity)
                                            }
                                        }
                                        feed.category?.let { category ->
                                            newsDao.findCategoryByCode(category)?.let { entity ->
                                                feed.categoryDTO = categoryDTOMapper.toDTO(entity)
                                            }
                                        }
                                    }
                                }
                                source?.let { source ->
                                    newsDao.findSourceByCode(source)?.let { entity ->
                                        sourceDTO = sourceDTOMapper.toDTO(entity)
                                    }
                                }
                                category?.let { category ->
                                    newsDao.findCategoryByCode(category)?.let { entity ->
                                        categoryDTO = categoryDTOMapper.toDTO(entity)
                                    }
                                }
                            }
                        }
                    }

                }

                override suspend fun storeFreshDataToLocal(data: List<FeedWithRelatedEntity>?): Boolean {
                    data?.let {
                        newsDao.deleteAllLatestFeed(FeedType.LATEST.toString())
                        newsDao.upsertAllFeedWithRelated(data)
                        return true
                    } ?: run {
                        return false
                    }
                }

                override suspend fun operateOnDataPostFetch(data: List<FeedDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getTopFeeds(): Deferred<Resource<List<FeedDTO>?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<List<FeedEntity>?, List<FeedDTO>?>(
                    "top_feed",
                    sharedPreferences,
                    "top_feed",
                    "top_feed",
                    TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getTopFeeds()
                }

                override suspend fun getDataFromLocal(): List<FeedEntity>? {
                    return newsDao.findTopFeeds()
                }

                override suspend fun convertApiResponseToData(response: Response<out Any?>): List<FeedEntity>? {
                    val newsResponse = response.body() as NewsResponse
                    return newsResponse.feedResponses.map {
                        feedResponseMapper.toEntity(
                            it, FeedType.TOP
                        )
                    }
                }

                override suspend fun convertDataToDto(data: List<FeedEntity>?): List<FeedDTO>? {
                    return data?.let { list ->
                        list.map {
                            feedDTOMapper.toDTO(it)
                        }
                    }
                }

                override suspend fun storeFreshDataToLocal(data: List<FeedEntity>?): Boolean {
                    data?.let {
                        newsDao.deleteAllTopFeed(FeedType.TOP.toString())
                        newsDao.insert(data)
                        return true
                    } ?: run {
                        return false
                    }
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
            object : DataFetchHelper.LocalFirstUntilStale<List<FeedEntity>?, List<FeedDTO>?>(
                "category_feed",
                sharedPreferences,
                "category_feed",
                category,
                TimeUnit.SECONDS.toSeconds(15) //storing news information for 15ø sec only.
            ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getFeedsByCategory(page, category)
                }

                override suspend fun getDataFromLocal(): List<FeedEntity>? {
                    return newsDao.findFeedsByCategory(category)
                }

                override suspend fun convertApiResponseToData(response: Response<out Any?>): List<FeedEntity>? {
                    val newsResponse = response.body() as NewsResponse
                    return newsResponse.feedResponses.map {
                        feedResponseMapper.toEntity(
                            it,
                            FeedType.CATEGORY
                        )
                    }
                }

                override suspend fun convertDataToDto(data: List<FeedEntity>?): List<FeedDTO>? {
                    return data?.let { list ->
                        list.map {
                            feedDTOMapper.toDTO(it)
                        }
                    }
                }

                override suspend fun storeFreshDataToLocal(data: List<FeedEntity>?): Boolean {
                    data?.let {
                        newsDao.deleteAllFeedByCategory(category)
                        newsDao.insert(data)
                        return true
                    } ?: run {
                        return false
                    }
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
                DataFetchHelper.NetworkFirstLocalFailover<List<SourceEntity>?, List<SourceDTO>?>(
                    "feed_sources"
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getSources()
                }

                override suspend fun getDataFromLocal(): List<SourceEntity>? {
                    return newsDao.findSources()
                }

                override suspend fun convertApiResponseToData(response: Response<out Any?>): List<SourceEntity>? {
                    val sourceResponse = response.body() as SourceResponse
                    return sourceResponse.sources.map {
                        sourceResponseMapper.toEntity(it)
                    }
                }

                override suspend fun convertDataToDto(data: List<SourceEntity>?): List<SourceDTO>? {
                    return data?.let { list ->
                        list.map {
                            sourceDTOMapper.toDTO(it)
                        }
                    }

                }

                override suspend fun storeFreshDataToLocal(data: List<SourceEntity>?): Boolean {
                    data?.let {
                        newsDao.deleteAllSources()
                        newsDao.insertSources(data)
                        return true
                    } ?: run {
                        return false
                    }
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
                DataFetchHelper.NetworkFirstLocalFailover<List<CategoryEntity>?, List<CategoryDTO>?>(
                    "feed_categories"
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getSources()
                }

                override suspend fun getDataFromLocal(): List<CategoryEntity>? {
                    return newsDao.findCategories()
                }

                override suspend fun convertApiResponseToData(response: Response<out Any?>): List<CategoryEntity>? {
                    val sourceResponse = response.body() as SourceResponse
                    return sourceResponse.categories.map {
                        categoryResponseMapper.toEntity(it)
                    }
                }

                override suspend fun convertDataToDto(data: List<CategoryEntity>?): List<CategoryDTO>? {
                    return data?.let { list ->
                        list.map {
                            categoryDTOMapper.toDTO(it)
                        }
                    }

                }

                override suspend fun storeFreshDataToLocal(data: List<CategoryEntity>?): Boolean {
                    data?.let {
                        newsDao.deleteAllCategories()
                        newsDao.insertCategories(data)
                        return true
                    } ?: run {
                        return false
                    }
                }

                override suspend fun operateOnDataPostFetch(data: List<CategoryDTO>?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

}