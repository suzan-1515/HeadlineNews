package com.cognota.feed.commons.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cognota.core.data.persistence.converter.BaseConverter
import com.cognota.feed.commons.data.local.dao.NewsDao
import com.cognota.feed.list.data.entity.FeedEntity

@Database(
    entities = [FeedEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BaseConverter::class)
abstract class NewsDb : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
