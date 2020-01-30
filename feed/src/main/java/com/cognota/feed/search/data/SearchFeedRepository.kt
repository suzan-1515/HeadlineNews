package com.cognota.feed.search.data

import com.cognota.core.data.model.api.ApiEmptyResponse
import com.cognota.core.data.model.api.ApiErrorResponse
import com.cognota.core.data.model.api.ApiResponse
import com.cognota.core.data.model.api.ApiSuccessResponse
import com.cognota.core.repository.BaseRepository
import com.cognota.core.vo.Resource
import com.cognota.core.vo.Status
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.TagDTOMapper
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class SearchFeedRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository,
    private val tagDTOMapper: TagDTOMapper
) : BaseRepository(), SearchFeedDataContract.Repository {


    @ExperimentalCoroutinesApi
    override suspend fun getFeeds(query: String): Flow<Resource<List<FeedDTO>?>> {
        return sourceAndCategoryRepository.getSourcesAndCategories().filter {
            it.status == Status.SUCCESS
        }.flatMapLatest {
            Timber.d("getFeeds")
            flow {
                emit(Resource.loading(null))

                when (val apiResponse = try {
                    val fetchFromNetwork = newsApiAPIService.searchFeeds(query)
                    ApiResponse.create(fetchFromNetwork)
                } catch (e: Throwable) {
                    ApiResponse.create<SearchFeedResponse>(e)
                }) {
                    is ApiSuccessResponse -> {
                        fun findSourceByCode(
                            source: String,
                            data: SourceAndCategoryDTO
                        ): SourceDTO {
                            return data.source?.find { it.code == source }?.let { it }
                                ?: kotlin.run {
                                    SourceDTO(
                                        code = source,
                                        id = 1,
                                        name = source,
                                        priority = 1
                                    )
                                }
                        }

                        fun findCategoryByCode(
                            category: String,
                            data: SourceAndCategoryDTO
                        ): CategoryDTO {
                            return data.category?.find { it.code == category }?.let { it }
                                ?: kotlin.run {
                                    CategoryDTO(
                                        code = category,
                                        enable = "",
                                        icon = "",
                                        id = 1,
                                        name = category,
                                        nameNp = category,
                                        priority = 1
                                    )
                                }
                        }

                        val feeds = apiResponse.body.hits?.hits?.map { hit ->
                            val sourceDTO =
                                findSourceByCode(hit.source.source, it.data!!)
                            val categoryDTO = findCategoryByCode(hit.source.category, it.data!!)

                            FeedDTO(
                                id = hit.id,
                                description = hit.source.description,
                                image = hit.source.image,
                                link = hit.source.link,
                                publishedDate = hit.source.pubDate,
                                title = hit.source.title,
                                updatedDate = hit.source.updateDate,
                                type = FeedType.SEARCH,
                                source = sourceDTO,
                                category = categoryDTO,
                                page = 1
                            )

                        }
                        emit(Resource.success(feeds))

                    }
                    is ApiEmptyResponse -> {
                        emit(Resource.success(listOf<FeedDTO>()))
                    }
                    is ApiErrorResponse -> {
                        emit(Resource.error(apiResponse.error, null))
                    }
                }
            }.flowOn(ioDispatcher)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getTags(): Flow<Resource<List<TagDTO>?>> {
        return newsDao.findTagsStream().filter { !it.isNullOrEmpty() }.distinctUntilChanged()
            .map { tags ->
                Resource.success(tags?.map {
                    tagDTOMapper.toDTO(it)
                })
            }.onStart {
                Resource.loading(null)
            }.flowOn(ioDispatcher)
    }

    override suspend fun getHistory(): Flow<Resource<List<TagDTO>?>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}