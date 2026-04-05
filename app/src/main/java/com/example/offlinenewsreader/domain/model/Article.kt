package com.example.offlinenewsreader.domain.model

data class Article(
    val title: String,
    val description: String?,
    val imageUrl: String,
    val url: String,
    val isBookMarked: Boolean = false
)