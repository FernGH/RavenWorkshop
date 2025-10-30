package com.example.ravenhackernews.core.usecases

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class UseCase<Params, R : Any> {

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCase(params: Params? = null): R

    protected fun addDisposable(disposable: Disposable?) {
        disposable?.let { disposables.add(it) }
    }

    fun clear() {
        if (!disposables.isDisposed) disposables.clear()
    }
}
