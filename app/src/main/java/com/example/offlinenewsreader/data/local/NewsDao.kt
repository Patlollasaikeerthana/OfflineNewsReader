package com.example.offlinenewsreader.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getNews(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<ArticleEntity>)

    @Query("UPDATE news SET isBookmarked = :value WHERE url = :url")
    suspend fun updateBookMark(url: String, value: Boolean)

    @Query(
        """
        SELECT * FROM news
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
    """
    )
    fun searchNews(query: String): Flow<List<ArticleEntity>>
}