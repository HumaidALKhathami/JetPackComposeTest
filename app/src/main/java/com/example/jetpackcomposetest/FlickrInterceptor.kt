package com.example.jetpackcomposetest

import com.example.jetpackcomposetest.common.Constants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

private const val FORMAT = "json"
private const val JSON_CALL_BACK = "1"
private const val EXTRAS = "url_s,description,date_taken"
private const val PER_PAGE = "10"

class FlickrInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl: HttpUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key", Constants.FLICKR_API_KEY)
            .addQueryParameter("format", FORMAT)
            .addQueryParameter("nojsoncallback", JSON_CALL_BACK)
            .addQueryParameter("extras", EXTRAS)
            .addQueryParameter("per_page", PER_PAGE)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}