package pl.movies.movieslist.ui.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import pl.movies.movieslist.ui.ext.loadImage

@BindingAdapter(
    requireAll = true,
    value = ["visible"]
)
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(
    value = ["image"]
)
fun setImage(view: ImageView, url: String?) = if (url != null) {

    view.loadImage(url)
} else Unit

@BindingAdapter(
    value = ["isSelected"]
)
fun setIsSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}
