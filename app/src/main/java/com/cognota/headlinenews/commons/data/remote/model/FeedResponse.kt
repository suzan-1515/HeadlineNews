package com.cognota.headlinenews.commons.data.remote.model


import com.cognota.core.data.model.api.ApiResponseObject
import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @SerializedName("author")
    val author: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("encloser_type")
    val encloserType: String?,
    @SerializedName("encloser_url")
    val encloserUrl: String?,
    @SerializedName("fetch_date")
    val fetchDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("link")
    val link: String,
    @SerializedName("pub_date")
    val pubDate: String,
    @SerializedName("related")
    val relatedFeedResponse: List<FeedResponse>?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("update_date")
    val updateDate: String,
    @SerializedName("uuid")
    val uuid: String?
) : ApiResponseObject