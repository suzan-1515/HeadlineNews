package com.cognota.headlinenews.list.di

import android.content.Context
import androidx.room.Room
import com.cognota.headlinenews.commons.data.local.dao.NewsDao
import com.cognota.headlinenews.commons.data.local.database.NewsDb
import dagger.Module
import dagger.Provides

@Module
class PersistanceModule {

    companion object {
        private const val NEWS_DATABASE_NAME = "news.db"
    }

    @ListScope
    @Provides
    fun provideNewsDatabase(applicationContext: Context): NewsDb {
        return Room.databaseBuilder(
            applicationContext.applicationContext,
            NewsDb::class.java,
            NEWS_DATABASE_NAME
        ).build()
    }

    @ListScope
    @Provides
    fun provideNewsDao(database: NewsDb): NewsDao {
        return database.newsDao()
    }
}