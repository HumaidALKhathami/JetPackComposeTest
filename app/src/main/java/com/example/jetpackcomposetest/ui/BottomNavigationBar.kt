package com.example.jetpackcomposetest.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetpackcomposetest.BottomNavItem

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    backStackEntry: State<NavBackStackEntry?>,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit,
    isVisible: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = isVisible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BottomNavigation(modifier = modifier, elevation = 5.dp) {
            items.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    icon = {
                        Column(horizontalAlignment = CenterHorizontally) {
                            Icon(imageVector = item.icon, contentDescription = item.name)
                            Text(text = item.name)
                        }
                    }
                )
            }
        }
    }
}