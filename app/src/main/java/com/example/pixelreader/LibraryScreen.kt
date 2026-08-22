package com.example.pixelreader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ПЕРЕЧИСЛЕНИЕ ВАРИАНТОВ СОРТИРОВКИ
enum class SortOption(val title: String) {
    DATE_ADDED("Дата добавления"),
    TITLE("Название"),
    AUTHOR("Автор"),
    ALBUM("Сборник"),
    RATING("Рейтинг"),
    READING_PROGRESS("Прогресс чтения")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(books: List<Book>) {
    var isGridView by remember { mutableStateOf(true) }
    var selectedCollection by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var selectedBookForPreview by remember { mutableStateOf<Book?>(null) }    // хранит выбранную книгу

    // Состояния для сортировки
    var showSortSheet by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf(SortOption.DATE_ADDED) }
    var isSortAscending by remember { mutableStateOf(false) }

    val collections = listOf("Классическая литература", "Научная фантастика", "Философия")

    Column(modifier = Modifier.fillMaxSize()) {

        LibraryHeader()

        LibrarySearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        CollectionFilters(
            collections = collections,
            selectedCollection = selectedCollection,
            onCollectionSelect = { selectedCollection = it }
        )

        LibraryControls(
            isGridView = isGridView,
            onGridClick = { isGridView = true },
            onListClick = { isGridView = false },
            onRandomClick = { /* Случайная книга */ },
            onSortClick = { showSortSheet = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(books) { book -> BookGridItem(book = book, onClick = { selectedBookForPreview = book }) }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                // ИСПРАВЛЕНО ЗДЕСЬ: добавлена закрывающая скобка '}'
                items(books) { book -> BookListItem(book = book, onClick = { selectedBookForPreview = book }) }
            }
        }
    }

    // ВЫЗОВ BOTTOM SHEET СОРТИРОВКИ
    if (showSortSheet) {
        SortBottomSheet(
            onDismiss = { showSortSheet = false },
            selectedOption = selectedSortOption,
            onOptionSelected = { selectedSortOption = it },
            isAscending = isSortAscending,
            onToggleDirection = { isSortAscending = !isSortAscending }
        )
    }

    // ВЫЗОВ BOTTOM SHEET ПРЕДПРОСМОТРА
    selectedBookForPreview?.let { book ->
        BookPreviewBottomSheet(
            book = book,
            availableCollections = collections,
            onDismiss = { selectedBookForPreview = null },
            onReadClick = {
                // TODO: Позже добавим переход на экран читалки
                selectedBookForPreview = null
            },
            onDeleteClick = {
                // TODO: Позже добавим логику удаления книги
                selectedBookForPreview = null
            }
        )
    }
}

// КОМПОНЕНТЫ ШТОРКИ СОРТИРОВКИ (BOTTOM SHEET)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    onDismiss: () -> Unit,
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
    isAscending: Boolean,
    onToggleDirection: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            Text(
                text = "Сортировать по",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
            )

            OrderToggleButton(isAscending = isAscending, onClick = onToggleDirection)

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SortOption.entries.forEach { option ->
                    SortOptionItem(
                        text = option.title,
                        isSelected = selectedOption == option,
                        onClick = { onOptionSelected(option) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderToggleButton(isAscending: Boolean, onClick: () -> Unit) {
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(contentColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAscending) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    tint = contentColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Порядок",
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.8f)
                )
                Text(
                    text = if (isAscending) "По возрастанию" else "По убыванию",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun SortOptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

// КОМПОНЕНТЫ ГЛАВНОГО ЭКРАНА
@Composable
fun LibraryHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Библиотека",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        FilledIconButton(
            onClick = { /* TODO: Смена темы */ },
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(Icons.Filled.DarkMode, contentDescription = "Смена темы")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), CircleShape),
        placeholder = { Text("Поиск книг...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Очистить")
                }
            }
        },
        shape = CircleShape,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun CollectionFilters(
    collections: List<String>,
    selectedCollection: String?,
    onCollectionSelect: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterPill(
                text = "Избранное",
                isSelected = selectedCollection == null,
                onClick = { onCollectionSelect(null) }
            )
        }
        items(collections) { collection ->
            FilterPill(
                text = collection,
                isSelected = selectedCollection == collection,
                onClick = { onCollectionSelect(collection) }
            )
        }
    }
}

@Composable
fun FilterPill(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        shape = CircleShape,
        color = bgColor,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        )
    }
}

@Composable
fun LibraryControls(
    isGridView: Boolean,
    onGridClick: () -> Unit,
    onListClick: () -> Unit,
    onRandomClick: () -> Unit,
    onSortClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = onRandomClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Icon(imageVector = Icons.Filled.Shuffle, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "СЛУЧАЙНАЯ КНИГА",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Кастомный Button Group
        LibraryButtonGroup(
            isGridView = isGridView,
            onSortClick = onSortClick,
            onGridClick = onGridClick,
            onListClick = onListClick
        )
    }
}

@Composable
fun LibraryButtonGroup(
    isGridView: Boolean,
    onSortClick: () -> Unit,
    onGridClick: () -> Unit,
    onListClick: () -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeContainerColor = MaterialTheme.colorScheme.primaryContainer
    val activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer

    Row(
        modifier = Modifier.height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // 1. Кнопка сортировки (левая, округлая слева)
        ButtonGroupItem(
            icon = Icons.Filled.Tune,
            isSelected = false,
            shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, topEnd = 8.dp, bottomEnd = 8.dp),
            onClick = onSortClick,
            containerColor = containerColor,
            contentColor = contentColor,
            activeContainerColor = activeContainerColor,
            activeContentColor = activeContentColor
        )
        // 2. Кнопка сетки (центр, квадратная)
        ButtonGroupItem(
            icon = Icons.Filled.GridView,
            isSelected = isGridView,
            shape = RoundedCornerShape(8.dp),
            onClick = onGridClick,
            containerColor = containerColor,
            contentColor = contentColor,
            activeContainerColor = activeContainerColor,
            activeContentColor = activeContentColor
        )
        // 3. Кнопка списка (правая, округлая справа)
        ButtonGroupItem(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            isSelected = !isGridView,
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp, topEnd = 24.dp, bottomEnd = 24.dp),
            onClick = onListClick,
            containerColor = containerColor,
            contentColor = contentColor,
            activeContainerColor = activeContainerColor,
            activeContentColor = activeContentColor
        )
    }
}

@Composable
fun ButtonGroupItem(
    icon: ImageVector,
    isSelected: Boolean,
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    activeContainerColor: Color,
    activeContentColor: Color
) {
    Surface(
        shape = shape,
        color = if (isSelected) activeContainerColor else containerColor,
        modifier = Modifier
            .width(48.dp)
            .fillMaxHeight()
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) activeContentColor else contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookPreviewBottomSheet(
    book: Book,
    availableCollections: List<String>,
    onDismiss: () -> Unit,
    onReadClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Состояние для выпадающего списка сборников
    var expanded by remember { mutableStateOf(false) }
    var selectedCollection by remember { mutableStateOf(book.collectionName ?: availableCollections.firstOrNull() ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 1. Шапка: Обложка, Название, Автор, Смена обложки
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Плейсхолдер обложки
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(width = 100.dp, height = 150.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Обложка", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Column {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )

                    FilledTonalButton(onClick = { /* Смена обложки */ }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Изменить обложку")
                    }
                }
            }

            // 2. Выбор сборника (Exposed Dropdown Menu)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCollection,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Сборник книг") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableCollections.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedCollection = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            // 3. Оценка (Звезды)
            Column {
                Text(
                    text = "ОЦЕНИТЬ КНИГУ (ДЛЯ СЕБЯ)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Оценка ${index + 1}",
                            tint = if (index < book.rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(36.dp).clickable { /* Обновить рейтинг */ }
                        )
                    }
                }
            }

            // 4. Описание (Синопсис)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHighest, // Контрастный контейнер
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Роман о бедном студенте Родионе Раскольникове, решившемся на страшное убийство ради проверки своей теории о «право имеющих» людях.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 5. Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Удалить")
                }

                Button(
                    onClick = onReadClick,
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Icon(imageVector = Icons.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Читать")
                }
            }
        }
    }
}