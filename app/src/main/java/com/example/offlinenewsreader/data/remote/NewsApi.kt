package com.example.offlinenewsreader.data.remote

import retrofit2.http.GET

interface NewsApi{
    @GET("top-headlines")
    suspend fun getNews(): NewsResponse
}