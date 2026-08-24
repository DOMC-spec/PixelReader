package com.example.pixelreader

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainScreen() {
    var currentTab by remember { mutableStateOf(NavigationTab.LIBRARY) }

    // тестовые данные: одна книга должна быть активна
    val sampleBook = Book(
        id = "1",
        title = "Преступление и наказание",
        author = "Фёдор Достоевский",
        format = "FB2",
        coverUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f",
        progress = 45,
        rating = 5,
        collectionName = "Классика",
        isActive = true // <-- АКТИВНАЯ КНИГА
    )

    val testLibrary = listOf(
        sampleBook,
        sampleBook.copy(id = "2", title = "Алиса в Стране чудес", author = "Льюис Кэрролл", progress = 10, isActive = false),
        sampleBook.copy(id = "3", title = "Межзвездный скиталец", author = "Джек Лондон", progress = 0, isActive = false)
    )

    val activeBook = testLibrary.find { it.isActive }

    Scaffold(
        bottomBar = {
            Column {
                if (activeBook != null) {
                    ActiveBookBanner(book = activeBook, onClick = { /* TODO: Открыть читалку */ })
                }
                PixelReaderBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (currentTab == NavigationTab.LIBRARY) {
                LibraryScreen(books = testLibrary)
            } else {
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
fun ActiveBookBanner(book: Book, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${book.author} • ${book.progress}% прочитано",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Продолжить чтение",
                    tint = MaterialTheme.colorScheme.onSurface
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