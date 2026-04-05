package com.example.offlinenewsreader.domain.usecase

import com.example.offlinenewsreader.domain.repository.NewsRepository

class SearchNewsUseCase(private val repo: NewsRepository) {
    operator fun invoke(query: String) = repo.searchNews(query)
}