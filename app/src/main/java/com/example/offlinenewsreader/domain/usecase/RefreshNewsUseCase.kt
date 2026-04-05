package com.example.offlinenewsreader.domain.usecase

import com.example.offlinenewsreader.domain.repository.NewsRepository

class RefreshNewsUseCase(private val repo: NewsRepository) {
    suspend operator fun invoke() { repo.refresh()}
}