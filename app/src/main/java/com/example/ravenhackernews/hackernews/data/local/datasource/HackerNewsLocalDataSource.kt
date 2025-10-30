package com.example.ravenhackernews.hackernews.data.local.datasource

import com.example.ravenhackernews.hackernews.data.local.HackerNewsDao
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsLocalSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HackerNewsDataLocalSource @Inject constructor(
    private val dao: HackerNewsDao
) : HackerNewsLocalSource {

    override fun getHackerNews(): Single<List<HackerNewEntity>> =
        dao.getAllIncludingDeleted()


    override fun updateAll(list: List<HackerNewEntity>): Completable =
        dao.updateAll(list)

    override fun softDelete(id: String): Completable =
        dao.softDelete(id)
}

