package com.example.offlinenewsreader.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.offlinenewsreader.data.repository.NetworkObserver
import com.example.offlinenewsreader.domain.model.Article
import com.example.offlinenewsreader.domain.repository.NewsRepository
import com.example.offlinenewsreader.domain.usecase.GetNewsUseCase
import com.example.offlinenewsreader.domain.usecase.GetPagedNewsUseCase
import com.example.offlinenewsreader.domain.usecase.RefreshNewsUseCase
import com.example.offlinenewsreader.domain.usecase.SearchNewsUseCase
import com.example.offlinenewsreader.presentation.state.NewsUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel(
    private val getNews: GetNewsUseCase,
    private val getPagedNews: GetPagedNewsUseCase,
    private val searchNews: SearchNewsUseCase,
    private val refresh: RefreshNewsUseCase,
    private val repo: NewsRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val _state = MutableStateFlow(NewsUiState())
    val state: StateFlow<NewsUiState> = _state

    val pagedArticles: Flow<PagingData<Article>> = getPagedNews()
        .cachedIn(viewModelScope)

    init {
        observe()
        observeNetwork()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observe() {
        viewModelScope.launch {
            query.debounce(300).distinctUntilChanged()
                .flatMapLatest { if (it.isBlank()) getNews() else searchNews(it) }
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

    private fun observeNetwork() {
        viewModelScope.launch {
            networkObserver.observe().collect { isOnline ->
                _state.update { it.copy(isOnline = isOnline) }
            }
        }
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
