package pl.movies.movieslist.ui.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import pl.movies.movieslist.R
import pl.movies.movieslist.util.ImagePath
import pl.movies.movieslist.util.withAppendedBaseUrl

fun <T : ImageView> T.loadImage(path: ImagePath) {
    Glide
        .with(this)
        .load(path.withAppendedBaseUrl())
        .placeholder(R.drawable.ic_baseline_image_24)
        .into(this)
}
