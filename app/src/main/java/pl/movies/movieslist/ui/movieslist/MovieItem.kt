@file:OptIn(ExperimentalCoilApi::class)

package pl.movies.movieslist.ui.movieslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.request.ImageRequest
import pl.movies.domain.nowplaying.MovieWithFavoriteStatus
import pl.movies.movieslist.R
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.ToggleMovieFavoriteStatus
import pl.movies.movieslist.util.withAppendedBaseUrl

@Composable
fun MovieItem(movie: MovieWithFavoriteStatus, onIntent: (MoviesListIntent) -> Unit) {
  Card(
    Modifier
      .height(330.dp)
      .padding(horizontal = 12.dp, vertical = 16.dp)
      .clickable {
        onIntent(MoviesListIntent.ShowMovieDetails(movie.id))
      },
  ) {
    Box {
      Column {
        AsyncImage(
          modifier = Modifier.requiredHeight(224.dp),
          model = ImageRequest.Builder(LocalContext.current)
            .data(movie.posterPath.withAppendedBaseUrl())
            .build(),
          contentScale = ContentScale.FillWidth,
          contentDescription = null
        )
        Text(
          modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 8.dp),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          text = movie.originalTitle
        )
      }
      IconButton(
        modifier = Modifier
          .padding(10.dp)
          .align(Alignment.TopEnd),
        onClick = { onIntent.invoke(ToggleMovieFavoriteStatus(movie.id)) }) {
        val starIconRes = if (movie.isFavorite) {
          R.drawable.ic_baseline_star_24
        } else {
          R.drawable.ic_baseline_star_border_24
        }
        Icon(
          modifier = Modifier
            .size(48.dp),
          painter = painterResource(starIconRes),
          // Use actual vector color
          tint = Color.Unspecified,
          contentDescription = null
        )
      }
    }
  }
}

val previewHandler = AsyncImagePreviewHandler {
  ColorImage(Color.Red.toArgb())
}

@Preview
@Composable
fun MovieItem_Preview() {
  CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
    MovieItem(
      MovieWithFavoriteStatus(
        id = 1L,
        posterPath = "/poster",
        adult = false,
        originalTitle = "Lorem Movie",
        isFavorite = false
      )
    ) {}
  }
}
