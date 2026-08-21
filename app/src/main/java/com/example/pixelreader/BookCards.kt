package com.example.pixelreader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ВЕРТИКАЛЬНАЯ КАРТОЧКА (СЕТКА)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookGridItem(book: Book, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Блок с обложкой
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Бейдж формата (EPUB, FB2 и т.д.)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = book.format,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Градиент и полоса прогресса поверх обложки снизу
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Прочитано", color = Color.White, fontSize = 10.sp)
                            Text("${book.progress}%", color = Color.White, fontSize = 10.sp)
                        }

                        Slider(
                            value = book.progress.toFloat(),
                            onValueChange = {},
                            valueRange = 0f..100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primaryContainer,
                                activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                inactiveTrackColor = Color.White.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            }

            // Блок с информацией (Название, Автор, Рейтинг)
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Бейдж коллекции (если есть)
                    if (book.collectionName != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = book.collectionName,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Звезды рейтинга
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = if (index < book.rating) Color(0xFFFFB825) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpressiveProgressIndicator(
    progress: Int,
    modifier: Modifier,
    activeColor: Color,
    inactiveColor: Color
) {

}

// ГОРИЗОНТАЛЬНАЯ КАРТОЧКА (СПИСОК)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListItem(book: Book, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Квадратная обложка размером 56dp
            Box(
                modifier = Modifier.size(56.dp)
            ) {
                Surface(shape = RoundedCornerShape(8.dp)) {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = book.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Маленький бейдж формата поверх картинки
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
                ) {
                    Text(
                        text = book.format,
                        color = Color(0xFFFFB2DD),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Текстовая информация
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = " • ${book.progress}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Открыть",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    @Composable
    fun ExpressiveProgressIndicator(
        progress: Int,
        modifier: Modifier = Modifier,
        activeColor: Color = MaterialTheme.colorScheme.primary,
        inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        val safeProgress = progress.coerceIn(0, 100)

        Row(
            modifier = modifier.height(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Активная часть (прочитано)
            if (safeProgress > 0) {
                Box(
                    modifier = Modifier
                        .weight(safeProgress.toFloat())
                        .height(6.dp)
                        .background(activeColor, RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.width(2.dp))
            }

            // Вертикальный ползунок
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(16.dp)
                    .background(activeColor, RoundedCornerShape(50))
            )

            // Неактивная часть (осталось)
            if (safeProgress < 100) {
                Spacer(modifier = Modifier.width(2.dp)) // Зазор
                Box(
                    modifier = Modifier
                        .weight((100 - safeProgress).toFloat())
                        .height(6.dp)
                        .background(inactiveColor.copy(alpha = 0.5f), RoundedCornerShape(50))
                )
            }
        }
    }
}