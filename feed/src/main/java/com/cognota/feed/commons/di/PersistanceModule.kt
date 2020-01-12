package com.cognota.feed.commons.di

import android.content.Context
import androidx.room.Room
import com.cognota.core.di.ModuleScope
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.database.NewsDb
import dagger.Module
import dagger.Provides

@Module
class PersistanceModule {

    companion object {
        private const val NEWS_DATABASE_NAME = "news.db"
    }

    @ModuleScope
    @Provides
    fun provideNewsDatabase(applicationContext: Context): NewsDb {
        return Room.databaseBuilder(
            applicationContext.applicationContext,
            NewsDb::class.java,
            NEWS_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @ModuleScope
    @Provides
    fun provideNewsDao(database: NewsDb): NewsDao {
        return database.newsDao()
    }
}