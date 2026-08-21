package com.example.pixelreader

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationTab(
    val title: String,
    val icon: ImageVector
) {
    LIBRARY("Библиотека", Icons.Filled.Book),
    COLLECTIONS("Коллекции", Icons.Filled.Favorite),
    SETTINGS("Настройки", Icons.Filled.Settings)
}