package com.cognota.feed.commons.data.remote.service

import com.cognota.feed.commons.data.remote.model.NewsResponse
import com.cognota.feed.commons.data.remote.model.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("/newsv3/infinite_feed.php")
    suspend fun getLatestFeeds(): Response<NewsResponse>

    @GET("/newsv2/feeds_top.php")
    suspend fun getTopFeeds(): Response<NewsResponse>

    @GET("/newsv3/infinite_feed.php")
    suspend fun getFeedsByCategory(@Query("page") page: Int, @Query("category") category: String): Response<NewsResponse>

    @GET("/newsv2/source.php")
    suspend fun getSources(): Response<SourceResponse>

}