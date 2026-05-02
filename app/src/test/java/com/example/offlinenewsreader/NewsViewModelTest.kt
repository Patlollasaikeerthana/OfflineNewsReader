package com.example.offlinenewsreader

import MainCoroutineRule
import app.cash.turbine.test
import com.example.offlinenewsreader.data.repository.NetworkObserver
import com.example.offlinenewsreader.domain.model.Article
import com.example.offlinenewsreader.domain.repository.NewsRepository
import com.example.offlinenewsreader.domain.usecase.GetNewsUseCase
import com.example.offlinenewsreader.domain.usecase.GetPagedNewsUseCase
import com.example.offlinenewsreader.domain.usecase.RefreshNewsUseCase
import com.example.offlinenewsreader.domain.usecase.SearchNewsUseCase
import com.example.offlinenewsreader.presentation.viewmodel.NewsViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeArticles = listOf(
        Article(
            title = "Test Article",
            description = "Test Desc",
            imageUrl = "https://img.com/test.jpg",
            url = "https://test.com",
            isBookMarked = false
        )
    )

    private lateinit var viewModel: NewsViewModel


    private val getNews = mockk<GetNewsUseCase>()
    private val searchNews = mockk<SearchNewsUseCase>()
    private val refresh = mockk<RefreshNewsUseCase>()
    private val repo = mockk<NewsRepository>()
    private val networkObserver = mockk<NetworkObserver>()
    private val getPagedNews = mockk<GetPagedNewsUseCase>()

    @Before
    fun setup() {
        every { getNews.invoke() } returns flowOf(fakeArticles)
        every { searchNews.invoke(any()) } returns flowOf(fakeArticles)
        coEvery { refresh.invoke() } just Runs
        coEvery { repo.toggleBookmark(any()) } just Runs
        every { networkObserver.observe() } returns flowOf(true)
        viewModel =
            NewsViewModel(getNews, getPagedNews, searchNews, refresh, repo, networkObserver)
    }

    @Test
    fun `initial load populates state with articles`() = runTest {
        testScheduler.advanceTimeBy(400)
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(fakeArticles, state.data)
            assertEquals(false, state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearch triggers search use case`() = runTest {
        viewModel.onSearch("test")
        testScheduler.advanceTimeBy(400)
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(fakeArticles, state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `offline state updates when network lost`() = runTest {
        val networkObserverOffline = mockk<NetworkObserver> {
            every { observe() } returns flowOf(false)
        }
        val offlineViewModel = NewsViewModel(
            getNews, getPagedNews, searchNews, refresh, repo, networkObserverOffline,
            )
        offlineViewModel.state.test {
            val state = awaitItem()
            assertEquals(false, state.isOnline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshNews updates loading state`() = runTest {
        viewModel.refreshNews()
        coVerify { refresh.invoke() }
    }

    @Test
    fun `toggleBookmark calls repo`() = runTest {
        viewModel.toggleBookMark("https://test.com")
        coVerify { repo.toggleBookmark("https://test.com") }
    }
}
