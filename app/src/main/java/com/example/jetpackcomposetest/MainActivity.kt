package com.example.jetpackcomposetest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.ui.BottomNavigationBar
import com.example.jetpackcomposetest.ui.Navigation
import com.example.jetpackcomposetest.ui.theme.JetPackComposeTestTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val pages = listOf(
        "Ver Urgent",
        "Urgent",
        "Normal"
    )

    private val bottomNavItems = listOf(
        BottomNavItem(
            name = "Home",
            route = "home",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = "Notifications",
            route = "notifications",
            icon = Icons.Default.Notifications
        ),
        BottomNavItem(
            name = "Search",
            route = "search",
            icon = Icons.Default.Search
        ),
        BottomNavItem(
            name = "Groups",
            route = "groups",
            icon = Icons.Default.Person
        )
    )

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetPackComposeTestTheme(
                darkTheme = Constants.isDarkMode.value
            ) {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = bottomNavItems,
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) {
                    Navigation(navController = navController, pages = pages)
                }
            }

        }
    }
}