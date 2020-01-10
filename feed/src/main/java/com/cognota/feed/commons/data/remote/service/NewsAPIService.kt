package com.cognota.feed.commons.data.remote.service

import com.cognota.feed.commons.data.remote.model.NewsFeedResponse
import com.cognota.feed.commons.data.remote.model.SourceResponse
import com.cognota.feed.commons.data.remote.model.TagResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("/newsv3/infinite_feed.php")
    suspend fun getLatestFeeds(): Response<NewsFeedResponse>

    @GET("/newsv2/feeds_top.php")
    suspend fun getTopFeeds(): Response<NewsFeedResponse>

    @GET("/newsv3/infinite_feed.php")
    suspend fun getFeedsByCategory(@Query("page") page: Int, @Query("category") category: String): Response<NewsFeedResponse>

    @GET("/newsv2/source.php")
    suspend fun getSources(): Response<SourceResponse>

    @GET("newsv3/tags.php")
    suspend fun getTags(): Response<TagResponse>

}