package com.example.ravenhackernews.hackernews.domain.usecase

import com.example.ravenhackernews.core.exceptions.UseCaseException
import com.example.ravenhackernews.core.usecases.CompletableUseCase
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@ViewModelScoped
class DeleteHackerNewsUseCase
    @Inject
    constructor(
        private val hackerNewsRepository: HackerNewsRepository,
    ) : CompletableUseCase<DeleteHackerNewsUseCase.DeleteHackerNewsUseCaseParams>() {
        public override fun buildUseCase(params: DeleteHackerNewsUseCaseParams?): Completable =
            params?.let {
                hackerNewsRepository.deleteNewById(it.idNew)
            } ?: Completable.error(UseCaseException.GenericException())

        data class DeleteHackerNewsUseCaseParams(
            val idNew: String,
        )
    }
