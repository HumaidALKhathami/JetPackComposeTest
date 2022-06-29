package com.example.jetpackcomposetest.flickrresponse

data class Photos(
    val page: Int,
    val pages: Int,
    val photo: List<Photo>,
)