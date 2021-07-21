package pl.movies.domain.nowplaying

import io.reactivex.rxjava3.core.Flowable
import pl.movies.domain.common.RxUsecase

class ObserveNowPlayingMoviesUsecase(): RxUsecase<Void, List<NowPlayingMovie>> {

    override fun execute(arguments: Void): Flowable<List<NowPlayingMovie>> {
        TODO("Not yet implemented")
    }
}
