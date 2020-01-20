package com.cognota.feed.commons.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @ColumnInfo(name = "category_code")
    val categoryCode: String,
    @ColumnInfo(name = "category_icon")
    val categoryIcon: String?,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "link")
    val link: String?,
    @ColumnInfo(name = "pub_date")
    val pubDate: String?,
    @ColumnInfo(name = "source_code")
    val sourceCode: String,
    @ColumnInfo(name = "source_favicon")
    val sourceFavicon: String?,
    @ColumnInfo(name = "source_icon")
    val sourceIcon: String?,
    @ColumnInfo(name = "source_name")
    val sourceName: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "update_date")
    val updateDate: String?
)