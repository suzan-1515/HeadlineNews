package com.cognota.feed.list.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val fetchDate: String?,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "pub_date")
    val pubDate: String?,
    @ColumnInfo(name = "source")
    val source: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "update_date")
    val updateDate: String?,
    @ColumnInfo(name = "uuid")
    val uuid: String?,
    @ColumnInfo(name = "related_feed")
    val relatedFeedId: String?,
    @ColumnInfo(name = "type")
    val type: String
)