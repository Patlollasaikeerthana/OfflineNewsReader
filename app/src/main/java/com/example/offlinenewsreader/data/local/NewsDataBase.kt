package com.example.offlinenewsreader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class,RemoteKeyEntity::class], version = 2)
abstract class NewsDataBase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteKeyDao():RemoteKeyDao
}
