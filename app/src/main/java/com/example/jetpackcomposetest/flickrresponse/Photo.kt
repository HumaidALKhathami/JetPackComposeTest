package com.example.jetpackcomposetest.flickrresponse

data class Photo(
    val id: String,
    val title: String,
    val url_s: String,
    val description: Description,
    val datetaken: String
)