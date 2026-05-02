package com.example.offlinenewsreader.domain.repository

import com.example.offlinenewsreader.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsPaged():Flow<PagingData<Article>>
    fun getNews(): Flow<List<Article>>
    fun searchNews(query: String): Flow<List<Article>>
    suspend fun refresh()
    suspend fun toggleBookmark(url: String)
}
