package com.example.ravenhackernews.hackernews.domain.usecase

import com.example.ravenhackernews.core.exceptions.UseCaseException
import com.example.ravenhackernews.core.usecases.SingleUseCase
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class RetrieveHackerNewsUseCase
    @Inject
    constructor(
        private val hackerNewsRepository: HackerNewsRepository,
    ) : SingleUseCase<RetrieveHackerNewsUseCase.RetrieveHackerNewsUseCaseParams, List<HackerNewDto>>() {
        public override fun buildUseCase(params: RetrieveHackerNewsUseCaseParams?): Single<List<HackerNewDto>> =
            params?.let {
                hackerNewsRepository.fetchHackerNews(it.query)
            } ?: Single.error(UseCaseException.GenericException())

        data class RetrieveHackerNewsUseCaseParams(
            val query: String,
        )
    }
