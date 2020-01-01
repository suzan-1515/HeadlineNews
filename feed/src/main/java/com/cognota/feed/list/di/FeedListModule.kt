package com.cognota.feed.list.di

import android.content.SharedPreferences
import com.cognota.core.di.FeatureScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.remote.service.NewsAPIService
import com.cognota.feed.list.adapter.PersonalizedFeedAdapter
import com.cognota.feed.list.data.ListDataContract
import com.cognota.feed.list.data.ListRepository
import com.cognota.feed.list.data.mapper.FeedDTOMapper
import com.cognota.feed.list.data.mapper.FeedResponseMapper
import com.cognota.feed.list.viewmodel.ListViewModelFactory
import com.squareup.picasso.Picasso
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
        feedDTOMapper: FeedDTOMapper
    ): ListDataContract.Repository = ListRepository(
        newsAPIService,
        newsDao,
        sharedPreferences,
        feedResponseMapper,
        feedDTOMapper
    )

    /*Repository*/
    @Provides
    @FeatureScope
    fun personalizedFeedAdapter(
        picasso: Picasso
    ) = PersonalizedFeedAdapter(picasso)

    @Provides
    @FeatureScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}