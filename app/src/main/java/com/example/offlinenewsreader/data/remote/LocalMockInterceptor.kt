package com.example.offlinenewsreader.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LocalMockInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val json = context.assets.open("news.json").bufferedReader().use { it.readText() }
        return Response.Builder().code(200).message("OK").request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(json.toByteArray().toResponseBody("application/json".toMediaType())).build()
    }
}