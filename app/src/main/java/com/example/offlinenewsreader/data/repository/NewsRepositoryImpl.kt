package com.example.offlinenewsreader.data.repository

import androidx.room.util.query
import com.example.offlinenewsreader.data.local.NewsDao
import com.example.offlinenewsreader.data.mapper.toDomain
import com.example.offlinenewsreader.data.mapper.toEntity
import com.example.offlinenewsreader.data.remote.NewsApi
import com.example.offlinenewsreader.domain.model.Article
import com.example.offlinenewsreader.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class NewsRepositoryImpl(
    private val api: NewsApi,
    private val dao: NewsDao
) : NewsRepository {
    override fun getNews(): Flow<List<Article>> {
        return networkBoundResource(
            query = { dao.getNews().map { it.map { e -> e.toDomain() } } },
            fetch = { api.getNews() },
            saveFetchResult = { response ->
                val bookmarkedMap = dao.getNews().first().associateBy { it.url }
                val entities = response.articles.map { dto ->
                    val isBookMarked = bookmarkedMap[dto.url]?.isBookMarked ?: false
                    dto.toEntity().copy(isBookMarked = isBookMarked)
                }
                dao.insertAll(entities)
            })
    }

    override fun searchNews(query: String): Flow<List<Article>> {
        return dao.searchNews(query).map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun refresh() {
        val response = api.getNews()
        val bookmarkedMap = dao.getNews().first().associateBy { it.url }
        val entities = response.articles.map { dto ->
            val isBookMarked = bookmarkedMap[dto.url]?.isBookMarked ?: false
            dto.toEntity().copy(isBookMarked = isBookMarked)
        }
        dao.insertAll(entities)
    }

    override suspend fun toggleBookmark(url: String) {
        val current = dao.getNews().first().find { it.url == url } ?: return
        dao.updateBookMark(url, !current.isBookMarked)
    }
}