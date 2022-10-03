package com.example.jetpackcomposetest

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposetest.common.Constants
import com.example.jetpackcomposetest.common.Screen
import com.example.jetpackcomposetest.ui.BottomNavigationBar
import com.example.jetpackcomposetest.ui.Navigation
import com.example.jetpackcomposetest.ui.theme.JetPackComposeTestTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
        }
        setContent {
            JetPackComposeTestTheme(
                darkTheme = Constants.isDarkMode.value
            ) {

                val pages = listOf(
                    getString(R.string.very_urgent),
                    getString(R.string.urgent),
                    getString(R.string.normal),
                    getString(R.string.videos)
                )

                val bottomNavItems = listOf(
                    BottomNavItem(
                        name = getString(R.string.home),
                        route = Screen.Home.route,
                        icon = Icons.Default.Home
                    ),
                    BottomNavItem(
                        name = getString(R.string.notifications),
                        route = Screen.Notifications.route,
                        icon = Icons.Default.Notifications
                    ),
                    BottomNavItem(
                        name = getString(R.string.search),
                        route = Screen.Search.route,
                        icon = Icons.Default.Search
                    ),
                    BottomNavItem(
                        name = getString(R.string.groups),
                        route = Screen.Groups.route,
                        icon = Icons.Default.Person
                    )
                )

                val navController = rememberNavController()

                val navBackStackEntry = navController.currentBackStackEntryAsState()

                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                when (navBackStackEntry.value?.destination?.route) {
                    Screen.Home.route -> bottomBarState.value = true
                    Screen.Search.route -> bottomBarState.value = true
                    Screen.Notifications.route -> bottomBarState.value = true
                    Screen.Groups.route -> bottomBarState.value = true
                    else -> bottomBarState.value = false
                }

                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = bottomNavItems,
                            backStackEntry = navBackStackEntry,
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
                            },
                            isVisible = bottomBarState
                        )
                    },
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        Navigation(navController = navController, pages = pages)
                    }
                }
            }

        }
    }
}