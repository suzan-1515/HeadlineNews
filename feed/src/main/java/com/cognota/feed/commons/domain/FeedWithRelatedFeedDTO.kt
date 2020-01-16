package com.cognota.feed.commons.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedWithRelatedFeedDTO(
    val feed: FeedDTO,
    val feedWithRelatedFeeds: List<RelatedFeedDTO>
) : Parcelable