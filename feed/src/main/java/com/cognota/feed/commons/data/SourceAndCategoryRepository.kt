package com.cognota.feed.commons.data

import android.content.SharedPreferences
import com.cognota.core.networking.NetworkBoundResource
import com.cognota.core.repository.BaseRepository
import com.cognota.core.util.RepositoryUtil
import com.cognota.core.vo.Resource
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.CategoryDTOMapper
import com.cognota.feed.commons.data.mapper.CategoryResponseMapper
import com.cognota.feed.commons.data.mapper.SourceDTOMapper
import com.cognota.feed.commons.data.mapper.SourceResponseMapper
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.CategoryDTO
import com.cognota.feed.commons.domain.SourceAndCategoryDTO
import com.cognota.feed.commons.domain.SourceDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.Response
import timber.log.Timber
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

    @ExperimentalCoroutinesApi
    @FlowPreview
    override suspend fun getSourcesAndCategories(): Flow<Resource<SourceAndCategoryDTO?>> {
        return object : NetworkBoundResource<SourceAndCategoryDTO?, SourceResponse>() {
            override suspend fun saveNetworkResult(item: SourceResponse) {
                Timber.d("saveNetworkResult")
                val sourcesEntity = item.sources.map {
                    sourceResponseMapper.toEntity(it)
                }
                val categoryEntity = item.categories.map {
                    categoryResponseMapper.toEntity(it)
                }
                if (!sourcesEntity.isNullOrEmpty())
                    newsDao.deleteAllSources()
                if (!categoryEntity.isNullOrEmpty())
                    newsDao.deleteAllCategories()
                newsDao.insertSources(sourcesEntity)
                newsDao.insertCategories(categoryEntity)

            }

            override fun shouldFetch(data: SourceAndCategoryDTO?): Boolean {
                return data == null ||
                        RepositoryUtil.shouldFetch(
                            sharedPreferences,
                            "feed_sources",
                            "feed_sources",
                            TimeUnit.SECONDS.toSeconds(120)
                        )
            }

            override fun loadFromDb(): Flow<SourceAndCategoryDTO?> {
                Timber.d("loadFromDb")
                return flow {
                    val findSources = newsDao.findSources()
                    val findCategories = newsDao.findCategories()
                    if (!findSources.isNullOrEmpty() && !findCategories.isNullOrEmpty()) {
                        Timber.d("source and category cache available")
                        emit(SourceAndCategoryDTO(
                            source = findSources.map { source -> sourceDTOMapper.toDTO(source) },
                            category = findCategories.map { category ->
                                categoryDTOMapper.toDTO(
                                    category
                                )
                            }
                        ))
                    } else {
                        Timber.d("source and category cache unavailable")
                        emit(null)
                    }
                }
            }

            override suspend fun fetchFromNetwork(): Response<SourceResponse> {
                Timber.d("fetchFromNetwork")
                return newsApiAPIService.getSources()
            }

            override fun onFetchFailed() {
                RepositoryUtil.resetCache(
                    sharedPreferences,
                    "feed_sources",
                    "feed_sources"
                )
            }

        }.asFlow().flowOn(ioDispatcher)

    }

    @ExperimentalCoroutinesApi
    override suspend fun getSources(): Flow<List<SourceDTO>?> {
        return newsDao.findSourcesReactive().filterNotNull().distinctUntilChanged().map { data ->
            data.map {
                sourceDTOMapper.toDTO(it)
            }
        }.flowOn(ioDispatcher)
    }

    @ExperimentalCoroutinesApi
    override suspend fun getCategories(): Flow<List<CategoryDTO>?> {
        return newsDao.findCategoriesReactive().filterNotNull().distinctUntilChanged().map { data ->
            data.map {
                categoryDTOMapper.toDTO(it)
            }
        }.flowOn(ioDispatcher)
    }
}