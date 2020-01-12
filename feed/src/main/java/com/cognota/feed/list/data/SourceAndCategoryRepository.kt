package com.cognota.feed.list.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.relation.SourceWithCategoryEntity
import com.cognota.feed.commons.data.mapper.CategoryDTOMapper
import com.cognota.feed.commons.data.mapper.CategoryResponseMapper
import com.cognota.feed.commons.data.mapper.SourceDTOMapper
import com.cognota.feed.commons.data.mapper.SourceResponseMapper
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import com.cognota.feed.commons.domain.SourceDTO
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SourceAndCategoryRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sharedPreferences: SharedPreferences,
    private val sourceResponseMapper: SourceResponseMapper,
    private val sourceDTOMapper: SourceDTOMapper,
    private val categoryResponseMapper: CategoryResponseMapper,
    private val categoryDTOMapper: CategoryDTOMapper
) : BaseRepository(), SourceAndCategoryDataContract.Repository {

    override suspend fun getSourcesAndCategories(): Deferred<Resource<SourceAndCategoryDTO?>> {
        val dataFetchHelper =
            object :
                DataFetchHelper.LocalFirstUntilStale<SourceWithCategoryEntity?, SourceAndCategoryDTO?>(
                    "feed_sources",
                    sharedPreferences,
                    "feed_sources",
                    "feed_sources",
                    TimeUnit.MINUTES.toMinutes(5) //storing news information for 15ø min only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getSources()
                }

                override suspend fun getDataFromLocal(): SourceWithCategoryEntity? {
                    return SourceWithCategoryEntity(
                        source = newsDao.findSources(),
                        category = newsDao.findCategories()
                    )
                }

                override suspend fun convertDataToDto(data: SourceWithCategoryEntity?): SourceAndCategoryDTO? {
                    return data?.let {
                        SourceAndCategoryDTO(
                            source = it.source?.map { source -> sourceDTOMapper.toDTO(source) },
                            category = it.category?.map { category ->
                                categoryDTOMapper.toDTO(
                                    category
                                )
                            }
                        )
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

                override suspend fun operateOnDataPostFetch(data: SourceAndCategoryDTO?) {
                    super.operateOnDataPostFetch(data)
                }
            }

        return dataFetchHelper.fetchDataIOAsync()
    }

    override suspend fun getSourcesStream(): Flow<List<SourceDTO>?> {
        return newsDao.findSourcesReactive().map { data ->
            data?.map {
                sourceDTOMapper.toDTO(it)
            }
        }
    }

    override suspend fun getCategoriesStream(): Flow<List<CategoryDTO>?> {
        return newsDao.findCategoriesReactive().map { data ->
            data?.map {
                categoryDTOMapper.toDTO(it)
            }
        }
    }
}