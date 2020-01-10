package com.cognota.feed.list.di

import android.content.SharedPreferences
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.mapper.*
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.list.data.ListDataContract
import com.cognota.feed.list.data.ListRepository
import com.cognota.feed.list.viewmodel.ListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FeedListModule {

    /*ViewModel*/
    @Provides
    @FeatureScope
    fun listViewModelFactory(
        repository: ListDataContract.Repository
    ): ListViewModelFactory = ListViewModelFactory(repository)

    /*Repository*/
    @Provides
    @FeatureScope
    fun listRepo(
        newsAPIService: NewsAPIService,
        newsDao: NewsDao,
        sharedPreferences: SharedPreferences,
        feedResponseMapper: FeedResponseMapper,
        feedDTOMapper: FeedDTOMapper,
        sourceResponseMapper: SourceResponseMapper,
        sourceDTOMapper: SourceDTOMapper,
        categoryResponseMapper: CategoryResponseMapper,
        categoryDTOMapper: CategoryDTOMapper,
        tagResponseMapper: TagResponseMapper,
        tagDTOMapper: TagDTOMapper
    ): ListDataContract.Repository = ListRepository(
        newsAPIService,
        newsDao,
        sharedPreferences,
        feedResponseMapper,
        feedDTOMapper,
        sourceResponseMapper,
        sourceDTOMapper,
        categoryResponseMapper,
        categoryDTOMapper,
        tagResponseMapper,
        tagDTOMapper
    )


    @Provides
    @FeatureScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}