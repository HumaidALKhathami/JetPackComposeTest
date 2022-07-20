package com.example.jetpackcomposetest.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun PostScreen(flickrViewModel: FlickrViewModel) {

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
            if (lazyPhotos.itemCount == 0)
                CircularProgressIndicator()
            else
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
                            loadState.refresh.endOfPaginationReached -> {

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
            modifier = Modifier.padding(4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ProfileImage(url = photo.url_s)
                Spacer(modifier = Modifier.size(8.dp))
                UserName(username = photo.title)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = photo.description._content)
            Spacer(modifier = Modifier.size(12.dp))
            ContentImage(url = photo.url_s)
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

@Composable
fun ProfileImage(url: String) {
    GlideImage(
        imageModel = url,
        requestOptions = { RequestOptions.overrideOf(1080, 720) },
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
    )
}

@Composable
fun ContentImage(url: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        GlideImage(
            imageModel = url,
            requestOptions = { RequestOptions.overrideOf(1080, 720) },
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun UserName(username: String) {
    Text(text = username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}