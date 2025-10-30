package com.example.ravenhackernews.hackernews.domain.datasource

import com.example.ravenhackernews.hackernews.data.remote.model.response.NewsResponse
import io.reactivex.rxjava3.core.Single

interface HackerNewsSource {
    fun getHackerNews(query: String?): Single<NewsResponse>
}

