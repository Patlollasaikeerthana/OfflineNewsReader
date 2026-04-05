package com.example.offlinenewsreader.domain.usecase

import com.example.offlinenewsreader.domain.repository.NewsRepository

class GetNewsUseCase(private val repo: NewsRepository) {
    operator fun invoke() = repo.getNews()
}