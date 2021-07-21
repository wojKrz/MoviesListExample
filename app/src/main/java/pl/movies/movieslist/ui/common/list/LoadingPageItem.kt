package pl.movies.movieslist.ui.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import pl.movies.movieslist.R
import pl.movies.movieslist.databinding.ItemLoadingPageBinding

class LoadingPageItem : AbstractBindingItem<ItemLoadingPageBinding>() {
    override val type: Int
        get() = R.id.itemLoading

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemLoadingPageBinding =
        ItemLoadingPageBinding.inflate(inflater, parent, false)
}
