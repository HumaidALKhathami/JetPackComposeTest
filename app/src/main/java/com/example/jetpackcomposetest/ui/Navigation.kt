package com.example.jetpackcomposetest.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpackcomposetest.common.Screen
import com.example.jetpackcomposetest.flickrresponse.Photo

@Composable
fun Navigation(navController: NavHostController, pages: List<String>) {
    NavHost(navController = navController, startDestination = Screen.OTP.route) {
        composable(Screen.Home.route) {
            HomeScreen(pages = pages, navController = navController)
        }
        composable(Screen.PostDetails.route + "/{photo}", arguments = listOf(
            navArgument("photo") {
                type = Photo.NavigationType
            }
        )) {
            val photo = it.arguments?.getParcelable<Photo>("photo")
            PostDetailsScreen(photo!!)
        }

        composable(Screen.Notifications.route) {
            Text(text = "notifications screen")
        }
        composable(Screen.Search.route) {
            Text(text = "Search Screen")
        }
        composable(Screen.Groups.route) {
            Text(text = "Groups Screen")
        }
        composable(Screen.OTP.route) {
            OTPScreen(navController)
        }
    }
}