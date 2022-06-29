package com.example.jetpackcomposetest.di

import com.example.jetpackcomposetest.FlickrApi
import com.example.jetpackcomposetest.FlickrInterceptor
import com.example.jetpackcomposetest.FlickrRepo
import com.example.jetpackcomposetest.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFlickrApi(): FlickrApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(FlickrInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.FLICKR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(FlickrApi::class.java)
    }

    @Provides
    @Singleton
    fun providesFlickrRepo(flickrApi: FlickrApi): FlickrRepo {
        return FlickrRepo(flickrApi)
    }
}