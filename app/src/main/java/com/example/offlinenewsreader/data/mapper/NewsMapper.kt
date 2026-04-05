package com.example.offlinenewsreader.data.mapper

import com.example.offlinenewsreader.data.local.ArticleEntity
import com.example.offlinenewsreader.data.remote.ArticleDto
import com.example.offlinenewsreader.domain.model.Article

fun ArticleDto.toEntity(): ArticleEntity {
    return ArticleEntity(
        url = url,
        title = title,
        imageUrl = imageUrl,
        description = description,
        isBookMarked = false
    )
}

fun ArticleEntity.toDomain(): Article {
    return Article(
        title = title,
        description = description,
        imageUrl = imageUrl,
        url = url,
        isBookMarked = isBookMarked
    )
}