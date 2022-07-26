package com.example.jetpackcomposetest

import android.os.Bundle
import androidx.navigation.NavType
import com.example.jetpackcomposetest.flickrresponse.PhotoArg
import com.google.gson.Gson


class PhotoType : NavType<PhotoArg>(
    false
) {
    override fun get(bundle: Bundle, key: String): PhotoArg? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): PhotoArg {
        return Gson().fromJson(value, PhotoArg::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: PhotoArg) {
        bundle.putParcelable(key, value)
    }
}