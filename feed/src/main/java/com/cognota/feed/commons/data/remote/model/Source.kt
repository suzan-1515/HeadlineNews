package com.cognota.feed.commons.data.remote.model


import com.cognota.core.data.model.api.ApiResponseObject
import com.google.gson.annotations.SerializedName

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