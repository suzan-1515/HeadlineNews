package com.cognota.feed.commons.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "feed")
data class FeedEntity(
    @ColumnInfo(name = "author")
    val author: String?,
    @ForeignKey
        (
        entity = CategoryEntity::class,
        parentColumns = ["code"],
        childColumns = ["category_code"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "category_code")
    var category: String?,
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
    @ForeignKey
        (
        entity = SourceEntity::class,
        parentColumns = ["code"],
        childColumns = ["source_code"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "source_code")
    val source: String?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "update_date")
    val updateDate: String?,
    @ColumnInfo(name = "uuid")
    val uuid: String?,
    @ColumnInfo(name = "type")
    val type: String,
    @ForeignKey
        (
        entity = FeedEntity::class,
        parentColumns = ["id"],
        childColumns = ["parent_id"],
        onDelete = ForeignKey.CASCADE
    )
    @ColumnInfo(name = "parent_id")
    val parentId: String?,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    @ColumnInfo(name = "page")
    val page: Int
)