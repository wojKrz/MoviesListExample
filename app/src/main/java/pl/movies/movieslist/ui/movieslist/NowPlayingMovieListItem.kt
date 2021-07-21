package pl.movies.movieslist.ui.movieslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import pl.movies.domain.nowplaying.NowPlayingMovie
import pl.movies.movieslist.R
import pl.movies.movieslist.databinding.ItemNowPlayingMovieBinding
import pl.movies.movieslist.ui.ext.loadImage

class NowPlayingMovieListItem(model: NowPlayingMovie) :
    ModelAbstractBindingItem<NowPlayingMovie, ItemNowPlayingMovieBinding>(model) {

    override val type: Int
        get() = R.id.itemNowPlaying

    override var identifier: Long
        get() = model.id
        set(_) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemNowPlayingMovieBinding =
        ItemNowPlayingMovieBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemNowPlayingMovieBinding, payloads: List<Any>) =
        with(binding) {
            titleText.text = model.originalTitle
            posterImage.loadImage(model.posterPath)
            favoriteToggle.isSelected = model.isFavorite
        }

    override fun unbindView(binding: ItemNowPlayingMovieBinding) =
        with(binding) {
            titleText.text = null
        }
}