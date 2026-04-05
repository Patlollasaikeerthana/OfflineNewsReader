package com.example.offlinenewsreader.presentation.viewmodel

import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offlinenewsreader.domain.repository.NewsRepository
import com.example.offlinenewsreader.domain.usecase.GetNewsUseCase
import com.example.offlinenewsreader.domain.usecase.RefreshNewsUseCase
import com.example.offlinenewsreader.domain.usecase.SearchNewsUseCase
import com.example.offlinenewsreader.presentation.state.NewsUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val getNews: GetNewsUseCase,
    private val searchNews: SearchNewsUseCase,
    private val refresh: RefreshNewsUseCase,
    private val repo: NewsRepository
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val _state = MutableStateFlow(NewsUiState())
    val state: StateFlow<NewsUiState> = _state

    init {
        observe()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observe() {
        viewModelScope.launch {
            query.debounce(300).flatMapLatest { if (it.isBlank()) getNews() else searchNews(it) }
                .collect { articles ->
                    _state.update {
                        it.copy(
                            data = articles,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onSearch(q: String) {
        query.value = q
    }

    fun refreshNews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                refresh.invoke()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Refresh Failed") }
            }
        }
    }

    fun toggleBookMark(url: String) {
        viewModelScope.launch {
            repo.toggleBookmark(url)
        }
    }
}