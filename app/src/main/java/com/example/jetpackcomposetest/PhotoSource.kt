package com.example.jetpackcomposetest

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetpackcomposetest.flickrresponse.Photo
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class PhotoSource(
    private val flickrRepo: FlickrRepo,
    private val query : String = ""
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val nextPage = params.key ?: 1
            val flickrResponse = if (query.isEmpty()){
                flickrRepo.getPhotos(nextPage)
            }else {
                withTimeout(5000){
                    flickrRepo.searchPhotos(query, nextPage)
                }
            }

            LoadResult.Page(
                data = flickrResponse.photos.photo,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nextPage == flickrResponse.photos.pages)null else flickrResponse.photos.page.plus(1)
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