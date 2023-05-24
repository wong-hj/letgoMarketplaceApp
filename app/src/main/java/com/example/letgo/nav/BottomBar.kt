package com.example.letgo.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBar("HomePage", "Discover", Icons.Default.Search)
    object Login: BottomBar("Login", "Login", Icons.Default.Lock)
}
