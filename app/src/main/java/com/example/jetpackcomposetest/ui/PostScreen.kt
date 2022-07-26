package com.example.jetpackcomposetest.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.request.RequestOptions
import com.example.jetpackcomposetest.FlickrViewModel
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.common.Screen
import com.example.jetpackcomposetest.flickrresponse.Photo
import com.example.jetpackcomposetest.flickrresponse.PhotoArg
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage

private const val TAG = "PostScreen"

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostScreen(flickrViewModel: FlickrViewModel, navController: NavController) {

    val lazyPagingItems = remember { flickrViewModel.getPhoto }

    val photos = lazyPagingItems.collectAsLazyPagingItems()

    val refreshState =
        rememberSwipeRefreshState(isRefreshing = photos.loadState.refresh is LoadState.Loading)


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        SwipeRefresh(
            state = refreshState,
            onRefresh = { photos.refresh() }
        ) {
//            if (photos.itemCount == 0)
//                Text(text = "Hello world!")
//            else
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
                items(photos) { photo ->
                    Post(photo = photo!!, navController)
                }
                photos.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            item {
                                Box {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = photos.loadState.refresh as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    modifier = Modifier,
                                    onClickRetry = { retry() }
                                )
                            }
                            Log.d(TAG, "PostScreen:localized ${e.error.localizedMessage}")
                            Log.d(TAG, "PostScreen:message ${e.error.message}")
                        }
                        loadState.append is LoadState.Error -> {
                            val e = photos.loadState.append as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    onClickRetry = { retry() }
                                )
                            }
                            Log.d(TAG, "PostScreen:localized ${e.error.localizedMessage}")
                            Log.d(TAG, "PostScreen:message ${e.error.message}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Post(photo: Photo, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val photoArg = PhotoArg(
                    photo.id,
                    photo.title,
                    photo.url_s,
                    photo.description._content,
                    photo.datetaken
                )
                navController.navigate(Screen.PostDetails.route + "?photo=$photoArg")
            }
            ,
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
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = photo.datetaken)
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