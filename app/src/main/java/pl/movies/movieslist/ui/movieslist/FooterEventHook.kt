package pl.movies.movieslist.ui.movieslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.EventHook
import pl.movies.movieslist.databinding.ItemFooterReloadBinding
import pl.movies.movieslist.ui.common.list.DataReloadType
import pl.movies.movieslist.ui.common.list.FooterReloadItem

class FooterEventHook(
    private val onActionClickListener: (DataReloadType) -> Unit
) : ClickEventHook<FooterReloadItem>() {

    override fun onBind(viewHolder: RecyclerView.ViewHolder): View? =
        if (viewHolder is BindingViewHolder<*> && viewHolder.binding is ItemFooterReloadBinding)
            (viewHolder.binding as ItemFooterReloadBinding).reloadButton
        else
            null

    override fun onClick(
        v: View,
        position: Int,
        fastAdapter: FastAdapter<FooterReloadItem>,
        item: FooterReloadItem
    ) {
        onActionClickListener(item.model)
    }
}
