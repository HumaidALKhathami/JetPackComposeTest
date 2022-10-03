package com.example.jetpackcomposetest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposetest.FlickrViewModel
import com.example.jetpackcomposetest.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(pages: List<String>, viewModel: FlickrViewModel = hiltViewModel(), navController: NavController) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column {TopAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
//        ProfileImage(url = Icons.Default.Home)
        Text(text = stringResource(id = R.string.home))
        Icon(imageVector = Icons.Default.Home, contentDescription = "")
    }
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    )
                )
            },
            backgroundColor = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            pages.forEachIndexed() { index, name ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(name) }
                )
            }
        }
        HorizontalPager(count = pages.size, state = pagerState) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {

                when(pagerState.currentPage) {
                    3 -> {
                        VideoScreen(navController = navController)
                    }
                    else -> {
                        Column {
                            PostScreen(viewModel.getPhoto, navController, false)
                        }
                    }
                }
            }
        }
    }
}