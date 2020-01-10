package com.cognota.feed.commons.data.remote.model


import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("version")
    val version: Int
)