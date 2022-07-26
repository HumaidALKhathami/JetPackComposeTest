package com.example.jetpackcomposetest.common

sealed class Screen(val route: String){
    object Home : Screen("home")
    object Notifications: Screen("notifications")
    object Groups: Screen("groups")
    object Search: Screen("search")
    object PostDetails: Screen("post_details")
    object OTP: Screen("one_time_password")
}
