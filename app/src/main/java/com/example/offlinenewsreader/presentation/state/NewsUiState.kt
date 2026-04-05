package com.example.offlinenewsreader.presentation.state

import com.example.offlinenewsreader.domain.model.Article

data class NewsUiState(
    val isLoading: Boolean = false,
    val data: List<Article> = emptyList(),
    val error: String? = null
)