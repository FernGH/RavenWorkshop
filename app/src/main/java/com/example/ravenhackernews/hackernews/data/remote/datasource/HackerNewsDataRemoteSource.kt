package com.example.ravenhackernews.hackernews.data.remote.datasource

import com.example.ravenhackernews.hackernews.data.remote.model.response.NewsResponse
import com.example.ravenhackernews.hackernews.data.service.HackerNewsService
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HackerNewsDataRemoteSource
    @Inject
    constructor(
        private val hackerNewsService: HackerNewsService,
    ) : HackerNewsSource {
        override fun getHackerNews(query: String?): Single<NewsResponse> = hackerNewsService.searchByDate(query)
    }
