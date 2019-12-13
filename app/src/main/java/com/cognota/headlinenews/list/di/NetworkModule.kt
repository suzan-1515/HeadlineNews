package com.cognota.headlinenews.list.di

import com.cognota.headlinenews.commons.data.remote.service.NewsAPIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    @ListScope
    fun postService(retrofit: Retrofit): NewsAPIService = retrofit.create(
        NewsAPIService::class.java
    )

}