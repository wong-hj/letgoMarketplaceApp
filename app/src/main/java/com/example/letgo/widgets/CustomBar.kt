package com.example.letgo.widgets

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.letgo.nav.BottomBar
import com.example.letgo.ui.theme.Typography
import kotlin.text.Typography

@Composable
fun CustomTopBar(navController: NavHostController, title: String, showBackIcon : Boolean) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = if (showBackIcon && navController.previousBackStackEntry != null) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            null
        }
    )
}

@Composable
fun CustomBottomBar(navController: NavController) {
    val items = listOf(
        BottomBar.Home,
        BottomBar.Liked,
        BottomBar.Offer,
        BottomBar.Profile
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon((item.icon), contentDescription = item.title, tint = MaterialTheme.colorScheme.primary) },
                label = { Text(text = item.title, style = Typography.body2, color = MaterialTheme.colorScheme.primary) },
                selectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.primaryContainer.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true

                    }
                }
            )
        }
    }
}