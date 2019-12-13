package com.cognota.headlinenews.list.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cognota.core.data.persistence.converter.BaseConverter
import java.util.*

@Entity(tableName = "feed")
data class FeedEntity(
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "category")
    val category: String?,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "encloser_type")
    val encloserType: String?,
    @ColumnInfo(name = "encloser_url")
    val encloserUrl: String?,
    @ColumnInfo(name = "fetch_date")
    @TypeConverters(BaseConverter::class)
    val fetchDate: Date,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "pub_date")
    @TypeConverters(BaseConverter::class)
    val pubDate: Date,
    @ColumnInfo(name = "source")
    val source: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @TypeConverters(BaseConverter::class)
    @ColumnInfo(name = "update_date")
    val updateDate: Date,
    @ColumnInfo(name = "uuid")
    val uuid: String?,
    @ColumnInfo(name = "related_feed")
    val relatedFeedId: String?,
    @ColumnInfo(name = "type")
    val type: String
)