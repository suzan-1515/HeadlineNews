package com.cognota.feed.commons.di

import com.cognota.core.di.ModuleScope
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Provides
    @ModuleScope
    fun postService(retrofit: Retrofit): NewsAPIService = retrofit.create(
        NewsAPIService::class.java
    )

}