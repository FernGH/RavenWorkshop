package com.example.ravenhackernews.hackernews.domain.datasource

import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface HackerNewsLocalSource {
    fun getHackerNews(): Single<List<HackerNewEntity>>
    fun updateAll(list: List<HackerNewEntity>): Completable
    fun softDelete(id: String): Completable
}
