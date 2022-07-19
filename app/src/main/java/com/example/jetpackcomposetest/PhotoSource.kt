package com.example.jetpackcomposetest

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetpackcomposetest.flickrresponse.Photo

class PhotoSource(
    private val flickrRepo: FlickrRepo
) : PagingSource<Int, Photo>() {

    override val keyReuseSupported: Boolean
        get() = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val nextPage = params.key ?: 1
            val flickrResponse = flickrRepo.getPhotos(nextPage)

            LoadResult.Page(
                data = flickrResponse.photos.photo,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = flickrResponse.photos.page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}