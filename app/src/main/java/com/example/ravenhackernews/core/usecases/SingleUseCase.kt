package com.example.ravenhackernews.core.usecases

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class SingleUseCase<Params, T : Any> : UseCase<Params, Single<T>>() {

    fun execute(params: Params? = null): Single<T> =
        buildUseCase(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
