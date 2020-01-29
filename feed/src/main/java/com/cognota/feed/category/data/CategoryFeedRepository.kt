package com.cognota.feed.category.data

import android.content.SharedPreferences
import com.cognota.core.networking.NetworkBoundResource
import com.cognota.core.repository.BaseRepository
import com.cognota.core.util.RepositoryUtil
import com.cognota.core.vo.Resource
import com.cognota.feed.commons.data.SourceAndCategoryDataContract
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.FeedDTO
import com.cognota.feed.commons.domain.FeedType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CategoryFeedRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sharedPreferences: SharedPreferences,
    private val sourceAndCategoryRepository: SourceAndCategoryDataContract.Repository,
    private val feedResponseMapper: FeedResponseMapper,
    private val feedDTOMapper: FeedDTOMapper
) : BaseRepository(), CategoryFeedDataContract.Repository {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun getFeedsByCategory(
        category: String,
        page: Int
    ): Flow<Resource<List<FeedDTO>?>> {
        return object : NetworkBoundResource<List<FeedDTO>?, NewsFeedResponse>() {
            override suspend fun saveNetworkResult(item: NewsFeedResponse) {

                val obj = item.feeds.map { feed ->
                    feedResponseMapper.toEntity(
                        feed, FeedType.CATEGORY, page
                    ).apply {
                        this.category = category
                    }
                }

                if (!obj.isNullOrEmpty()) {
                    newsDao.deleteAllFeedByCategory(category)
                }

                newsDao.insert(obj)
            }

            override fun shouldFetch(data: List<FeedDTO>?): Boolean {
                return data.isNullOrEmpty() ||
                        RepositoryUtil.shouldFetch(
                            sharedPreferences,
                            "category_feed$category$page",
                            "latest_feed",
                            TimeUnit.SECONDS.toSeconds(120)
                        )
            }

            override fun loadFromDb(): Flow<List<FeedDTO>?> {
                return flow {
                    emit(
                        newsDao.findFeedsByCategory(page, category)?.map { feed ->
                            feedDTOMapper.toDTO(feed)
                        }
                    )

                }
            }

            override suspend fun fetchFromNetwork(): Response<NewsFeedResponse> {
                return newsApiAPIService.getFeedsByCategory(
                    page,
                    category
                )
            }

            override fun onFetchFailed() {
                RepositoryUtil.resetCache(
                    sharedPreferences,
                    "category_feed$category$page",
                    "category_feed"
                )
            }

        }.asFlow().flowOn(ioDispatcher)
    }

}