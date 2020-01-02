package com.cognota.feed.commons.data.remote.model


import com.cognota.core.data.model.api.ApiResponseObject
import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("sources")
    val sources: List<Source>
) : ApiResponseObject