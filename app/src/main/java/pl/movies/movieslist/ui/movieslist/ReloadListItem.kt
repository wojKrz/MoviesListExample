package pl.movies.movieslist.ui.movieslist

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ReloadListItem(infoText: String, buttonText: String, onReloadClick: () -> Unit) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(infoText)
    Button(onReloadClick) {
      Text(buttonText)
    }
  }
}

@Composable
@Preview
fun ReloadListItem_Preview() {
  ReloadListItem("End of list", "Click to reload") {}
}
