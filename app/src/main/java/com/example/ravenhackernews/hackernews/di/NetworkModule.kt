package com.example.ravenhackernews.hackernews.di

import com.example.ravenhackernews.hackernews.data.service.HackerNewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HackerNewsModule {

    private const val BASE_URL = "https://hn.algolia.com/"

    @Provides
    @Singleton
    fun provideHackerNewsRetrofit(baseBuilder: Retrofit.Builder): Retrofit =
        baseBuilder
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideHackerNewsService(retrofit: Retrofit): HackerNewsService =
        retrofit.create(HackerNewsService::class.java)
}
