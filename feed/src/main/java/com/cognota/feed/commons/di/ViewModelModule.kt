package com.cognota.feed.commons.di

import com.cognota.core.di.ModuleScope
import com.cognota.feed.bookmark.data.BookmarkDataContract
import com.cognota.feed.bookmark.viewmodel.BookmarkFeedViewModelFactory
import com.cognota.feed.option.viewmodel.FeedOptionViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    /*ViewModel*/
    @Provides
    @ModuleScope
    fun bookmarkViewModelFactory(
        repository: BookmarkDataContract.Repository
    ): BookmarkFeedViewModelFactory =
        BookmarkFeedViewModelFactory(
            repository
        )

    /*ViewModel*/
    @Provides
    @ModuleScope
    fun feedOptionViewModelFactory(
        repository: BookmarkDataContract.Repository
    ): FeedOptionViewModelFactory =
        FeedOptionViewModelFactory(
            repository
        )


}