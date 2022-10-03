package com.example.jetpackcomposetest

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetpackcomposetest.flickrresponse.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.internal.wait
import javax.inject.Inject

private const val TAG = "SearchViewModel"
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val flickrRepo : FlickrRepo
): ViewModel() {

    var searchPhotos = searchPhotos()

    var index = 0
    var offset = 0

    private val _query = mutableStateOf("")
    val query: State<String> = _query

    fun updateQuery(newQuery: String){
        _query.value = newQuery
        searchPhotos = searchPhotos()
    }


    private fun searchPhotos() : Flow<PagingData<Photo>> = Pager(PagingConfig(10)){
        PhotoSource(flickrRepo, query.value)
    }.flow
        .cachedIn(viewModelScope)
}