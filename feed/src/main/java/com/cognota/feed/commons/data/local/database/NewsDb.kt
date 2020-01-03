package com.cognota.feed.commons.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.CategoryEntity
import com.cognota.feed.commons.data.local.entity.FeedEntity
import com.cognota.feed.commons.data.local.entity.RelatedFeedEntity
import com.cognota.feed.commons.data.local.entity.SourceEntity

@Database(
    entities = [FeedEntity::class, RelatedFeedEntity::class, SourceEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDb : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
