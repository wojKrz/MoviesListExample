package pl.movies.domain.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface Usecase<Args, Result> {
    fun execute(arguments: Args): Result
}

interface RxUsecase<Args, Result>: Usecase<Args, Flowable<Result>>

interface CompletableRxUsecase<Args>: Usecase<Args, Completable>
