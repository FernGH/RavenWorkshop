package com.example.ravenhackernews.hackernews.domain.repository

import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface HackerNewsRepository {
    fun fetchHackerNews(query: String?): Single<List<HackerNewDto>>
    fun deleteNewById(id: String): Completable
}
