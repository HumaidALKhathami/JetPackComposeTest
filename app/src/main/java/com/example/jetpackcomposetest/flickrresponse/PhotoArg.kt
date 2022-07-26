package com.example.jetpackcomposetest.flickrresponse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoArg(
    val id: String,
    val title: String,
    val url_s: String,
    val description: String,
    val datetaken: String
) : Parcelable