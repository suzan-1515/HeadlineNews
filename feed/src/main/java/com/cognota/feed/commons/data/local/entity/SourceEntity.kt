package com.cognota.feed.commons.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "source")
data class SourceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "favicon")
    val favicon: String?,
    @ColumnInfo(name = "icon")
    val icon: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "priority")
    val priority: Int,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean
)