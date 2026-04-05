package com.example.offlinenewsreader.di

import org.koin.androidx.viewmodel.dsl.viewModel
import androidx.room.Room
import com.example.offlinenewsreader.data.local.NewsDataBase
import com.example.offlinenewsreader.data.remote.LocalMockInterceptor
import com.example.offlinenewsreader.data.remote.NewsApi
import com.example.offlinenewsreader.data.repository.NewsRepositoryImpl
import com.example.offlinenewsreader.domain.repository.NewsRepository
import com.example.offlinenewsreader.domain.usecase.GetNewsUseCase
import com.example.offlinenewsreader.domain.usecase.RefreshNewsUseCase
import com.example.offlinenewsreader.domain.usecase.SearchNewsUseCase
import com.example.offlinenewsreader.presentation.viewmodel.NewsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val interceptor = LocalMockInterceptor(get())
        val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
        Retrofit.Builder().baseUrl("https://dummyurl.com/").client(client).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }
    single<NewsApi> {
        get<Retrofit>().create(NewsApi::class.java)
    }
    single {
        Room.databaseBuilder(
            get(),
            NewsDataBase::class.java,
            "news_db"
        ).build()
    }
    single { get<NewsDataBase>().newsDao() }
    single<NewsRepository> {
        NewsRepositoryImpl(get(), get())
    }
    factory { GetNewsUseCase(get()) }
    factory { RefreshNewsUseCase(get()) }
    factory { SearchNewsUseCase(get()) }
    viewModel{ NewsViewModel(get(), get(), get(), get()) }
}