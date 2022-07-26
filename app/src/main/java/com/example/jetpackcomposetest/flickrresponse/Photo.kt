package com.example.jetpackcomposetest.flickrresponse

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val title: String,
    val url_s: String,
    val description: Description,
    val datetaken: String
): Parcelable {
    companion object NavigationType : NavType<Photo>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Photo? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): Photo {
            return Gson().fromJson(value, Photo::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: Photo) {
            bundle.putParcelable(key, value)
        }
    }
}