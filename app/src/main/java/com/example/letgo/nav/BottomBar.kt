package com.example.letgo.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBar("HomePage", "Discover", Icons.Default.Search)
    object Liked: BottomBar("Liked", "Liked", Icons.Default.Favorite)
    object Profile: BottomBar("Profile", "Profile", Icons.Default.Person)
}
