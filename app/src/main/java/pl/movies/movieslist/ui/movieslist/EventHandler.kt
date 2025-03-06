package pl.movies.movieslist.ui.movieslist

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object EventHandler {

  private val _events = Channel<Event>(Channel.BUFFERED)
  val events = _events.receiveAsFlow()

  fun triggerEvent(event: Event) {
    CoroutineScope(Dispatchers.Default).launch { _events.send(event) }
  }

  sealed class Event {
    data class ShowMovieDetails(val movieId: Long): Event()
  }
}
