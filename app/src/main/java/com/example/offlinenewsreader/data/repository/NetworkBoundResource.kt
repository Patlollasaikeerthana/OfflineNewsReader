package com.example.offlinenewsreader.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult:suspend (RequestType)->Unit,
    crossinline onFetchFailed:(Throwable)->Unit={}
): Flow<ResultType> = flow{
    emitAll(query())

    try{
        val response=fetch()
        saveFetchResult(response)
    }
    catch (t: Throwable){
        onFetchFailed(t)
    }
    emitAll(query())
}