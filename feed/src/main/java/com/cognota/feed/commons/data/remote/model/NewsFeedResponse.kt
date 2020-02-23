package com.cognota.feed.commons.data.remote.model


import com.google.gson.annotations.SerializedName

data class NewsFeedResponse(
    @SerializedName("feeds")
    val feeds: List<Feed>,
    @SerializedName("version")
    val version: Int
) {
    data class Feed(
        @SerializedName("author")
        val author: String?,
        @SerializedName("category")
        val category: String?,
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
        val link: String?,
        @SerializedName("pub_date")
        val pubDate: String,
        @SerializedName("related")
        val related: List<Related>?,
        @SerializedName("source")
        val source: String?,
        @SerializedName("title")
        val title: String,
        @SerializedName("update_date")
        val updateDate: String,
        @SerializedName("uuid")
        val uuid: String
    ) {
        data class Related(
            @SerializedName("author")
            val author: String?,
            @SerializedName("category")
            val category: String?,
            @SerializedName("content")
            val content: String,
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
            val link: String?,
            @SerializedName("pub_date")
            val pubDate: String,
            @SerializedName("source")
            val source: String?,
            @SerializedName("title")
            val title: String,
            @SerializedName("update_date")
            val updateDate: String,
            @SerializedName("uuid")
            val uuid: String
        )
    }
}