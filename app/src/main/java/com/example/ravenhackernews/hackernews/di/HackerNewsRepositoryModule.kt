package com.example.ravenhackernews.hackernews.di

import com.example.ravenhackernews.hackernews.data.local.datasource.HackerNewsDataLocalSource
import com.example.ravenhackernews.hackernews.data.remote.datasource.HackerNewsDataRemoteSource
import com.example.ravenhackernews.hackernews.data.repository.HackerNewsDataRepository
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsLocalSource
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsSource
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HackerNewsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteSource(
        source: HackerNewsDataRemoteSource
    ): HackerNewsSource

    @Binds
    @Singleton
    abstract fun bindLocalSource(
        source: HackerNewsDataLocalSource
    ): HackerNewsLocalSource

    @Binds
    @Singleton
    abstract fun provideHackerNewsRepository(
        repository: HackerNewsDataRepository
    ): HackerNewsRepository
}
