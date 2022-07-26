package com.example.jetpackcomposetest

import android.util.Log
import com.example.jetpackcomposetest.flickrresponse.FlickrResponse
import javax.inject.Inject


private const val TAG = "FlickrRepo"
class FlickrRepo @Inject constructor(
    private val flickrApi: FlickrApi
) {
    suspend fun getPhotos(pageNumber: Int): FlickrResponse {
        Log.d(TAG, "getPhotos: new Request $pageNumber")
       return flickrApi.getPhotos(pageNumber)
    }
}