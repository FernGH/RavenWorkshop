package com.example.ravenhackernews.core.usecases

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class CompletableUseCase<Params> : UseCase<Params, Completable>() {
    fun execute(params: Params? = null): Completable =
        buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
