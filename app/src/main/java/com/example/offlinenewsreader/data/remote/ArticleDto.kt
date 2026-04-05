package com.example.offlinenewsreader.data.remote

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    val title: String,
    val description: String?,
    @SerializedName("urlToImage")
    val imageUrl: String,
    val url: String
)