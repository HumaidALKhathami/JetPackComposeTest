package com.example.jetpackcomposetest.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.request.RequestOptions
import com.example.jetpackcomposetest.FlickrViewModel
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.flickrresponse.Photo
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostScreen(flickrViewModel: FlickrViewModel = hiltViewModel()) {

    val lazyPhotos = flickrViewModel.getPhoto.collectAsLazyPagingItems()

    val refreshState =
        rememberSwipeRefreshState(isRefreshing = lazyPhotos.loadState.refresh is LoadState.Loading)


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        SwipeRefresh(
            state = refreshState,
            onRefresh = { lazyPhotos.refresh() }
        ) {
            LazyColumn() {
                item {
                    Button(
                        onClick = {
                            Constants.isDarkMode.value = !Constants.isDarkMode.value
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(text = "Change mode")
                    }
                }
                items(lazyPhotos) { photo ->
                    Post(photo = photo!!)
                }
                lazyPhotos.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { CircularProgressIndicator() }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = lazyPhotos.loadState.refresh as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    modifier = Modifier,
                                    onClickRetry = { retry() }
                                )
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = lazyPhotos.loadState.append as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    onClickRetry = { retry() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Post(photo: Photo) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(text = photo.title)

            GlideImage(
                imageModel = photo.url_s,
                requestOptions = { RequestOptions.overrideOf(1080, 720) })
        }
    }
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = Color.Red
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Try again")
        }
    }
}