package com.cognota.feed.commons.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "enable")
    val enable: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "name_np")
    val nameNp: String,
    @ColumnInfo(name = "priority")
    val priority: Int
)