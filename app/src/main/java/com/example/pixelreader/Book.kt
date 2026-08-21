package com.example.pixelreader

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val format: String,
    val coverUrl: String,
    val progress: Int,
    val rating: Int,
    val collectionName: String? = null
)