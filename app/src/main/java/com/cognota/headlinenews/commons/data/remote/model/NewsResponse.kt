package com.cognota.headlinenews.commons.data.remote.model


import com.cognota.core.data.model.api.ApiResponseObject
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("feeds")
    val feedResponses: List<FeedResponse> = listOf(),
    @SerializedName("version")
    val version: Int = 0
) : ApiResponseObject