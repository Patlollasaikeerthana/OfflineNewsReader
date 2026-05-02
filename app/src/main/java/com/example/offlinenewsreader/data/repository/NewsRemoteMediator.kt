package com.example.offlinenewsreader.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.offlinenewsreader.data.local.ArticleEntity
import com.example.offlinenewsreader.data.local.NewsDao
import com.example.offlinenewsreader.data.local.NewsDataBase
import com.example.offlinenewsreader.data.local.RemoteKeyDao
import com.example.offlinenewsreader.data.local.RemoteKeyEntity
import com.example.offlinenewsreader.data.mapper.toEntity
import com.example.offlinenewsreader.data.remote.NewsApi
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.flow.first
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val api: NewsApi,
    private val dao: NewsDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val db: NewsDataBase
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun initialize(): InitializeAction {
        // refresh cache every time app opens
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {

        return try {
            // figure out which page to load
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    // we don't support prepend
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    // get next page from remote keys
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val remoteKey = remoteKeyDao.getKey(lastItem.url)
                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // fetch from network (mock interceptor returns same JSON always)
            val response = api.getNews()
            val articles = response.articles

            // preserve bookmarks on refresh
            val bookmarkedUrls = dao.getNews().first()
                .filter { it.isBookMarked }
                .map { it.url }
                .toSet()

            // write to DB in transaction
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val nextPage = if (articles.isEmpty()) null else page + 1
                val prevPage = if (page == 1) null else page - 1

                val keys = articles.map { dto ->
                    RemoteKeyEntity(
                        url = dto.url,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                val entities = articles.map { dto ->
                    dto.toEntity().copy(
                        isBookMarked = dto.url in bookmarkedUrls,
                        page = page
                    )
                }

                remoteKeyDao.insertAll(keys)
                dao.insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
