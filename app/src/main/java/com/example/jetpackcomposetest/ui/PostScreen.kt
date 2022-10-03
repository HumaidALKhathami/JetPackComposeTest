package com.example.jetpackcomposetest.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.example.jetpackcomposetest.R
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.common.Screen
import com.example.jetpackcomposetest.flickrresponse.Photo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val TAG = "PostScreen"

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostScreen(
    lazyPagingItems: Flow<PagingData<Photo>>,
    navController: NavController,
    isSearchPage: Boolean,
    listState: LazyListState = rememberLazyListState()
) {

    val photos = lazyPagingItems.collectAsLazyPagingItems()

    val refreshState = if (photos.itemCount != 0)
        rememberSwipeRefreshState(isRefreshing = photos.loadState.refresh is LoadState.Loading)
    else rememberSwipeRefreshState(isRefreshing = false)

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        SwipeRefresh(
            state = refreshState,
            onRefresh = { photos.refresh() }
        ) {
            if (photos.itemCount == 0 && photos.loadState.append !is LoadState.Loading)
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else
            LazyColumn(
                state = listState,
            ) {
                items(photos) { photo ->
                    Log.d(TAG, "PostScreen: $photo")
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
                            if (photos.itemCount != 0 && !isSearchPage){
                            item {
                                Box {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                            }else{
                                item {
                                    ErrorItem(
                                        message = stringResource(id = R.string.search_error_message),
                                        modifier = Modifier,
                                        onClickRetry = { retry() }
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
                        }
                        loadState.append is LoadState.Error -> {
                            val e = photos.loadState.append as LoadState.Error
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
fun Post(photo: Photo, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val photoJson = Uri.encode(Gson().toJson(photo))
                navController.navigate(Screen.PostDetails.route + "/${photoJson}")
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
            ContentImage(url = photo.url_s, navController = navController)
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
//            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6,
            color = Color.Red
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(id = R.string.try_again))
        }
    }
}

@Composable
fun ProfileImage(url: String) {
    val painter = rememberAsyncImagePainter(model = url)

    Image(painter = painter,
        contentDescription = "" ,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape))
}

@Composable
fun ContentImage(url: String, navController: NavController, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(model = url, placeholder = painterResource(id = R.drawable.ic_baseline_adb_24))
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(painter = if (url.isEmpty()) painterResource(id = R.drawable.ic_baseline_adb_24) else painter,
            contentDescription = "" ,
            modifier = modifier
                .height(180.dp)
                .fillMaxWidth(0.9f)
                .clip(shape = RoundedCornerShape(2.dp))
                .align(Alignment.Center)
                .clickable {
                    if (url.isNotEmpty()) {
                        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.ImageScreen.route + "/${encodedUrl}")
                    }
                },
            contentScale = ContentScale.Crop
            )
    }
}

@Composable
fun UserName(username: String) {
    Text(text = username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}