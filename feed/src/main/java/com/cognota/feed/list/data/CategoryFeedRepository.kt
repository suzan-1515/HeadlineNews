package com.cognota.feed.list.data

import android.content.SharedPreferences
import com.cognota.core.networking.DataFetchHelper
import com.cognota.core.repository.BaseRepository
import com.cognota.core.repository.Resource
import com.cognota.feed.commons.data.local.FeedType
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.relation.FeedWithSourcesEntity
import com.cognota.feed.commons.data.mapper.FeedDTOMapper
import com.cognota.feed.commons.data.mapper.FeedResponseMapper
import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.commons.domain.FeedDTO
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CategoryFeedRepository @Inject constructor(
    private val newsApiAPIService: NewsAPIService,
    private val newsDao: NewsDao,
    private val sharedPreferences: SharedPreferences,
    private val feedResponseMapper: FeedResponseMapper,
    private val feedDTOMapper: FeedDTOMapper
) : BaseRepository(), CategoryFeedDataContract.Repository {

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
                    TimeUnit.MINUTES.toMinutes(2) //storing news information for 15ø sec only.
                ) {

                override suspend fun getDataFromNetwork(): Response<out Any?> {
                    return newsApiAPIService.getFeedsByCategory(page, category)
                }

                override suspend fun getDataFromLocal(): List<FeedWithSourcesEntity>? {
                    return newsDao.findFeedsByCategory(page, category)
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
                            feed, FeedType.CATEGORY, page
                        ).apply {
                            this.category = category
                        }
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

}