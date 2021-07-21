package pl.movies.movieslist.ui.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import pl.movies.movieslist.R
import pl.movies.movieslist.databinding.ItemFooterReloadBinding

sealed class DataReloadType constructor(
    @StringRes val messageRes: Int,
    @StringRes val buttonTextRes: Int
) {
    object NoMorePages : DataReloadType(R.string.no_more_pages, R.string.no_more_pages_reload)
    object PagingError : DataReloadType(R.string.common_error, R.string.paging_error_try_again)
    class CustomType(@StringRes messageRes: Int, @StringRes buttonTextRes: Int) :
        DataReloadType(messageRes, buttonTextRes)
}

class FooterReloadItem(message: DataReloadType) :
    ModelAbstractBindingItem<DataReloadType, ItemFooterReloadBinding>(message) {

    override val type: Int
        get() = R.id.itemFooterReload

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemFooterReloadBinding =
        ItemFooterReloadBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemFooterReloadBinding, payloads: List<Any>) =
        with(binding) {
            val resources = messageText.resources

            messageText.text = resources.getText(model.messageRes)
            reloadButton.text = resources.getText(model.buttonTextRes)
        }
}