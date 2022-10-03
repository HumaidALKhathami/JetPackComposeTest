package com.example.jetpackcomposetest

import com.example.jetpackcomposetest.flickrresponse.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList")
    suspend fun getPhotos(
        @Query("page") pageNumber: Int
    ): FlickrResponse

    @GET("services/rest/?method=flickr.photos.search")
    suspend fun searchPhotos(
        @Query("text") query:String,
        @Query("page") pageNumber: Int
    ): FlickrResponse
}