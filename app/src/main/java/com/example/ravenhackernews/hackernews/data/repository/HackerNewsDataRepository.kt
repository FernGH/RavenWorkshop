package com.example.ravenhackernews.hackernews.data.repository

import com.example.ravenhackernews.core.extesions.parseError
import com.example.ravenhackernews.hackernews.data.mapper.HackerNewToEntityMapper
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsLocalSource
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsSource
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import com.example.ravenhackernews.hackernews.domain.mapper.HackerEntityToDtoMapper
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HackerNewsDataRepository @Inject constructor(
    private val remoteSource: HackerNewsSource,
    private val localSource: HackerNewsLocalSource,
    private val mapperToEntity: HackerNewToEntityMapper,
    private val mapperToDto: HackerEntityToDtoMapper,
) : HackerNewsRepository {

    override fun fetchHackerNews(query: String?): Single<List<HackerNewDto>> =
        remoteSource
            .getHackerNews(query)
            .parseError()
            .subscribeOn(Schedulers.io())
            .map { response ->
                response.hits.map { mapperToEntity.transform(it) }
            }
            .flatMap { network ->
                localSource
                    .getHackerNews()
                    .map { currentList -> currentList.associateBy { it.id } }
                    .flatMap { currentLocal ->
                        val merged = network.map { remote ->
                            val prev = currentLocal[remote.id]
                            if (prev != null) remote.copy(isDeleted = prev.isDeleted) else remote
                        }

                        localSource
                            .updateAll(merged)
                            .andThen(
                                Single.just(
                                    merged
                                        .filter { !it.isDeleted }
                                        .map { entity -> mapperToDto.transform(entity) }
                                )
                            )
                    }
            }
            .onErrorResumeNext { throwable ->
                localSource
                    .getHackerNews()
                    .map { list ->
                        list
                            .filter { !it.isDeleted }
                            .map { mapperToDto.transform(it) }
                    }
                    .flatMap { localList ->
                        if (localList.isEmpty()) Single.error(throwable)
                        else Single.just(localList)
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())

    override fun deleteNewById(id: String): Completable =
        localSource.softDelete(id)
}
