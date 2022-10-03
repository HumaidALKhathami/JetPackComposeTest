package com.example.jetpackcomposetest.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpackcomposetest.R
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
            PostDetailsScreen(photo = photo!!, navController = navController)
        }

        composable(Screen.Notifications.route) {
            Text(text = stringResource(id = R.string.notification_screen))
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Groups.route) {
            GroupsScreen(navController = navController)
        }
        composable(Screen.OTP.route) {
            OTPScreen(navController)
        }
        composable(Screen.ImageScreen.route + "/{imageUrl}" , arguments = listOf(
            navArgument("imageUrl"){
                type = NavType.StringType
            }
        )){
            val url = it.arguments?.getString("imageUrl")
            ImageScreen(url = url!!, navController = navController)
        }
        composable(Screen.VideoPreview.route + "/{videoUrl}" + "/{time}" , arguments = listOf(
            navArgument("videoUrl"){
                type = NavType.StringType
            },
            navArgument("time"){
                type = NavType.LongType
            }
        )){
            val url = it.arguments?.getString("videoUrl")
            val time = it.arguments?.getLong("time")
            VideoPreview(url = url!!, time = time!!)
        }
    }
}