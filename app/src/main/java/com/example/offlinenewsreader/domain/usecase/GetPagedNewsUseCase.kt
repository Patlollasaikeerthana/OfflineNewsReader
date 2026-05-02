package com.example.offlinenewsreader.domain.usecase

import androidx.paging.PagingData
import com.example.offlinenewsreader.domain.model.Article
import com.example.offlinenewsreader.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetPagedNewsUseCase(private val repo: NewsRepository) {
    operator fun invoke(): Flow<PagingData<Article>> = repo.getNewsPaged()
}
