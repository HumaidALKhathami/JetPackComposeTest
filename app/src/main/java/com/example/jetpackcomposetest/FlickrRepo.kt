package com.example.jetpackcomposetest

import com.example.jetpackcomposetest.flickrresponse.FlickrResponse
import javax.inject.Inject

class FlickrRepo @Inject constructor(
    private val flickrApi: FlickrApi
) {
    suspend fun getPhotos(pageNumber: Int): FlickrResponse = flickrApi.getPhotos(pageNumber)
}