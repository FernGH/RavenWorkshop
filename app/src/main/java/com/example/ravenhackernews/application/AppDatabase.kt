package com.example.ravenhackernews.application

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ravenhackernews.hackernews.data.local.HackerNewsDao
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity

@Database(
    entities = [HackerNewEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): HackerNewsDao
}
