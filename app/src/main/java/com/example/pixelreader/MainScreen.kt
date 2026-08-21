package com.ваш_домен.pixelreader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pixelreader.Book
import com.example.pixelreader.BookGridItem
import com.example.pixelreader.BookListItem
import com.example.pixelreader.LibraryScreen
import com.example.pixelreader.NavigationTab

@Composable
fun MainScreen() {
    var currentTab by remember { mutableStateOf(NavigationTab.LIBRARY) }

    Scaffold(
        bottomBar = {
            PixelReaderBottomBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (currentTab == NavigationTab.LIBRARY) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentTab == NavigationTab.LIBRARY) {

                        // тестовая книга
                        val sampleBook = Book(
                            id = "1",
                            title = "Преступление и наказание",
                            author = "Фёдор Достоевский",
                            format = "FB2",
                            coverUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f",
                            progress = 45,
                            rating = 5,
                            collectionName = "Классика"
                        )

                        // список из трех книг для сетки/списка
                        val testLibrary = listOf(
                            sampleBook,
                            sampleBook.copy(id = "2", title = "Алиса в Стране чудес", author = "Льюис Кэрролл", progress = 10),
                            sampleBook.copy(id = "3", title = "Межзвездный скиталец", author = "Джек Лондон", progress = 85)
                        )

                        // экран Библиотеки
                        LibraryScreen(books = testLibrary)

                    }
                }
            } else {
                // Заглушка для остальных вкладок
                Text(
                    text = "Текущий экран: ${currentTab.title}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun PixelReaderBottomBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        NavigationTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(imageVector = tab.icon, contentDescription = tab.title)
                },
                label = {
                    Text(text = tab.title, style = MaterialTheme.typography.labelMedium)
                },
                alwaysShowLabel = false
            )
        }
    }
}