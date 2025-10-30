package com.example.ravenhackernews.hackernews.di

import android.content.Context
import androidx.room.Room
import com.example.ravenhackernews.application.AppDatabase
import com.example.ravenhackernews.hackernews.data.local.HackerNewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun db(
        @ApplicationContext ctx: Context,
    ): AppDatabase = Room.databaseBuilder(ctx, AppDatabase::class.java, "raven.db").build()

    @Provides
    fun news(db: AppDatabase): HackerNewsDao = db.newsDao()
}
