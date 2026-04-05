package com.example.offlinenewsreader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class ArticleEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val isBookMarked: Boolean = false
)