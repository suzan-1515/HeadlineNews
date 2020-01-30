package com.cognota.feed.search.data

import com.google.gson.annotations.SerializedName

data class SearchFeedResponse(
    @SerializedName("hits")
    val hits: Hits?
) {
    data class Hits(
        @SerializedName("hits")
        val hits: List<Hit>?
    ) {
        data class Hit(
            @SerializedName("_id")
            val id: String,
            @SerializedName("_source")
            val source: Source,
            @SerializedName("_version")
            val version: Int?
        ) {

            data class Source(
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
                val fetchDate: String?,
                @SerializedName("image")
                val image: String?,
                @SerializedName("link")
                val link: String?,
                @SerializedName("pub_date")
                val pubDate: String?,
                @SerializedName("source")
                val source: String,
                @SerializedName("title")
                val title: String,
                @SerializedName("update_date")
                val updateDate: String?,
                @SerializedName("uuid")
                val uuid: String?
            )
        }
    }
}