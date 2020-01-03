package com.cognota.feed.commons.data.remote.model


import com.cognota.core.data.model.api.ApiResponseObject
import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("sources")
    val sources: List<Source>
) : ApiResponseObject {
    data class Source(
        @SerializedName("code")
        val code: String,
        @SerializedName("favicon")
        val favicon: String?,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("priority")
        val priority: Int
    ) : ApiResponseObject

    data class Category(
        @SerializedName("code")
        val code: String,
        @SerializedName("enable")
        val enable: String,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_np")
        val nameNp: String,
        @SerializedName("priority")
        val priority: Int
    ) : ApiResponseObject
}