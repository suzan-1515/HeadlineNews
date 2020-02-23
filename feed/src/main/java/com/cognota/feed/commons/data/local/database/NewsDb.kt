package com.cognota.feed.commons.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cognota.feed.commons.data.local.converter.DateTimeConverter
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.commons.data.local.entity.*

@Database(
    entities = [FeedEntity::class, SourceEntity::class, CategoryEntity::class, TagEntity::class, BookmarkEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class NewsDb : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
