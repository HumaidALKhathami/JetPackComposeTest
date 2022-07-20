package com.example.jetpackcomposetest.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Navigation(navController: NavHostController, pages: List<String>) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(pages = pages)
        }
        composable("notifications") {
            Text(text = "notifications screen")
        }
        composable("search") {
            Text(text = "Search Screen")
        }
        composable("Groups") {
            Text(text = "Groups Screen")
        }
    }
}