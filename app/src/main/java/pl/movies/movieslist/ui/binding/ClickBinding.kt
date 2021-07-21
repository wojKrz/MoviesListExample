package pl.movies.movieslist.ui.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.movies.movieslist.util.addTo

@BindingAdapter(
    requireAll = false,
    value = ["disposables", "throttledClicks"]
)
fun setClicks(
    view: View, disposables: CompositeDisposable, throttledClicks: View.OnClickListener?
) {
    if (throttledClicks != null) {
        view
            .clicks()
            .subscribe { throttledClicks.onClick(view) }
            .addTo(disposables)
    }
}
