package com.cognota.headlinenews.commons.data.remote.service

import com.cognota.headlinenews.commons.data.remote.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("/newsv3/infinite_feed.php")
    suspend fun getLatestFeeds(): Response<NewsResponse>

    @GET("/newsv2/feeds_top.php")
    suspend fun getTopFeeds(): Response<NewsResponse>

    @GET("/newsv2/infinite_feed.php")
    suspend fun getFeedsByCategory(@Query("category") category: String): Response<NewsResponse>

}