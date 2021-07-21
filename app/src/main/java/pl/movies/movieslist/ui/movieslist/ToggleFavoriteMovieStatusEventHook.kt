package pl.movies.movieslist.ui.movieslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.listeners.ClickEventHook
import pl.movies.movieslist.databinding.ItemNowPlayingMovieBinding

class ToggleFavoriteMovieStatusEventHook(
    private val onToggleFavoriteClickListener: (Long) -> Unit
) : ClickEventHook<NowPlayingMovieListItem>() {

    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? =
        if (viewHolder is BindingViewHolder<*> && viewHolder.binding is ItemNowPlayingMovieBinding)
            (viewHolder.binding as ItemNowPlayingMovieBinding).favoriteToggle
        else
            null

    override fun onClick(
        v: View,
        position: Int,
        fastAdapter: FastAdapter<NowPlayingMovieListItem>,
        item: NowPlayingMovieListItem
    ) {
        onToggleFavoriteClickListener(item.model.id)
    }
}
