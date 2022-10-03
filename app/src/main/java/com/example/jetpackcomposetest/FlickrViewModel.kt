package com.example.jetpackcomposetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetpackcomposetest.flickrresponse.Photo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FlickrViewModel @Inject constructor(
    private val flickrRepo: FlickrRepo
) : ViewModel() {
    val getPhoto: Flow<PagingData<Photo>> = Pager(PagingConfig(10)) {
        PhotoSource(flickrRepo)
    }.flow
        .cachedIn(viewModelScope)
}