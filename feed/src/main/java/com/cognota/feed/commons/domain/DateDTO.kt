package com.cognota.feed.commons.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateDTO(
    val parsed: String,
    val raw: String
) : Parcelable