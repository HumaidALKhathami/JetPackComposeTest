package com.example.jetpackcomposetest.flickrresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Description(
    val _content: String
): Parcelable