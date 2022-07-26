package com.example.jetpackcomposetest.flickrresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val title: String,
    val url_s: String,
    val description: Description,
    val datetaken: String
): Parcelable